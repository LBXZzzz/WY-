package com.example.topviewap.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roompart.historyData.HistoryData
import com.example.roompart.historyData.HistoryDataRoom
import com.example.topviewap.R
import com.example.topviewap.adapter.HotDataRecyclerView
import com.example.topviewap.entries.Data
import com.example.topviewap.entries.Song
import com.example.topviewap.examples.Repository
import com.example.topviewap.utils.SongDataPagingSource
import com.example.topviewap.widget.WaterFlowLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {

    private val TAG = "SearchActivity"

    private lateinit var mToolbar: Toolbar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mWaterFlowLayout: WaterFlowLayout
    private lateinit var mProgressBar: ProgressBar//加载条
    private lateinit var mLlySong: LinearLayout //搜索出来歌曲rv的LinearLayout
    private lateinit var mLlyHot: LinearLayout //热搜榜单的LinearLayout
    private lateinit var mEd: AppCompatEditText
    private lateinit var mScrollView: ScrollView
    private lateinit var mTextView: TextView

    //懒加载技术来获取HotSearchViewModel的实例
    private val viewModel by lazy { ViewModelProvider(this).get(HotSearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        mToolbar.title = ""
        setSupportActionBar(mToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_return)
        }
        mProgressBar.visibility = View.GONE
        mLlySong.visibility = View.GONE
        initWaterFlowLayout()
        viewModel.hotSearch()//获取热搜数据
        initEditText()
        viewModel.hotSearchLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                viewModel.hotDataList.addAll(data)
                initHotRank()
            }
        })
        viewModel.searchLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                viewModel.dataList.addAll(data)
            }
        })

    }

    //内部使用的各个fun
    private fun init() {
        mToolbar = findViewById(R.id.search_too_bar)
        mRecyclerView = findViewById(R.id.rv_search_activity)
        mWaterFlowLayout = findViewById(R.id.wf_search)
        mProgressBar = findViewById(R.id.pb_search)
        mLlySong = findViewById(R.id.lly_search_recyclerview)
        mLlyHot = findViewById(R.id.lly_hot)
        mEd = findViewById(R.id.search_et)
        mScrollView = findViewById(R.id.scrollView)
        mTextView = findViewById(R.id.history)
    }

    /**
     * 初始化热搜榜
     */
    private fun initHotRank() {
        //初始化热搜榜单
        for (i in 0 until viewModel.hotDataList.size) {
            val number = (i + 1).toString() + ". "
            val bt = Button(this)
            bt.text = viewModel.hotDataList[i].searchWord
            if (i < 3) {
                bt.setTextColor(resources.getColor(R.color.big_red))
            }
            bt.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            bt.setPadding(50, 0, 0, 0)
            bt.setBackgroundColor(resources.getColor(R.color.white))
            mLlyHot.addView(bt)
            bt.setOnClickListener(View.OnClickListener {
                SongDataPagingSource.songList.clear()
                val key = bt.text.toString()
                mLlyHot.visibility = View.GONE
                mScrollView.visibility = View.GONE
                mLlySong.visibility = View.VISIBLE
                viewModel.search(key)
                val historyDataRoom = HistoryDataRoom(this)
                val historyData = HistoryData()
                historyData.data = key
                historyDataRoom.insert(historyData)
                initRecyclerView(key)
            })
        }
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView(key: String) {

        //对recyclerView做一些初始化操作
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager
        val adapter = HotDataRecyclerView(viewModel.dataList, applicationContext)
        adapter.mOnItemClickListener = object : HotDataRecyclerView.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                MusicActivity.isRoom = false
                val intent = Intent(this@SearchActivity, MusicActivity::class.java).apply {
                    putExtra("song", SongDataPagingSource.songList[position])
                }
                startActivity(intent)
            }
        }
        mRecyclerView.adapter = adapter
        mProgressBar.visibility = View.GONE
        mLlySong.visibility = View.VISIBLE
        //这个函数是触发Paging 3分页功能的核心，调用这个函数之后，Paging 3就开始工作了
        //collect()函数是一个挂起函数，只有在协程作用域中才能调用它
        lifecycleScope.launch {
            viewModel.getPagingData(key).collect { pagingData ->
                //viewModel.dataList1.addAll(pagingData)
                adapter.submitData(pagingData)
            }
        }
    }

    //初始化流式布局
    private fun initWaterFlowLayout() {
        val historyDataRoom = HistoryDataRoom(this)
        val datas = historyDataRoom.queryAll()
        if (datas!!.isEmpty()) {
            mTextView.visibility = View.GONE
            mWaterFlowLayout.visibility = View.GONE
        } else {
            val layoutInflater = LayoutInflater.from(this)
            for (i in datas.indices) {
                val textView = layoutInflater.inflate(
                    R.layout.waterflow_layout_view,
                    mWaterFlowLayout,
                    false
                ) as Button
                textView.text = datas[datas.size - i - 1].data
                textView.setOnClickListener() {
                    val key = textView.text.toString()
                    mLlyHot.visibility = View.GONE
                    mScrollView.visibility = View.GONE
                    mLlySong.visibility = View.VISIBLE
                    viewModel.search(key)
                    initRecyclerView(key)
                }
                mWaterFlowLayout.addView(textView)
            }
        }

    }

    private fun initEditText() {
        mEd.setOnEditorActionListener { v, actionId, event ->
            SongDataPagingSource.songList.clear()
            val key = mEd.text.toString()
            mLlyHot.visibility = View.GONE
            mScrollView.visibility = View.GONE
            mLlySong.visibility = View.GONE
            mWaterFlowLayout.visibility = View.GONE
            mProgressBar.visibility = View.VISIBLE
            viewModel.search(key)
            val historyData = HistoryData()
            historyData.data = key
            val historyDataRoom = HistoryDataRoom(this)
            historyDataRoom.insert(historyData)
            initRecyclerView(key)
            //点击回车后自动收起键盘
            val manager =
                applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    //ViewModel层
    class HotSearchViewModel : ViewModel() {
        private val hotSearchData = MutableLiveData<String>()
        private val searchData = MutableLiveData<String>()
        private var offset = 0

        //对界面的数据进行保存
        //热搜数据
        val hotDataList = mutableListOf<Data>()

        //搜索数据
        val dataList = mutableListOf<Song>()

        //使用Transformations的switchMap()方法来观察这个对象，否则仓库层返回的LiveData对象将无法进行观察
        val hotSearchLiveData = Transformations.switchMap(hotSearchData) {
            Repository.hotSearch()
        }

        val searchLiveData = Transformations.switchMap(searchData) { key ->
            offset += 30
            Repository.searchData(key, offset)
        }

        fun hotSearch() {
            hotSearchData.value = ""
        }

        fun search(key: String) {
            searchData.value = key
        }

        fun getPagingData(key: String): Flow<PagingData<Song>> {
            return Repository.getPagingData(key).cachedIn(viewModelScope)
        }

    }
}