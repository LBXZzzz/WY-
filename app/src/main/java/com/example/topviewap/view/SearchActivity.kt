package com.example.topviewap.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topviewap.R
import com.example.topviewap.adapter.HotDataRecyclerView
import com.example.topviewap.entries.Data
import com.example.topviewap.examples.Repository
import com.example.topviewap.widget.WaterFlowLayout

class SearchActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mWaterFlowLayout:WaterFlowLayout

    //懒加载技术来获取HotSearchViewModel的实例
    private val viewModel by lazy { ViewModelProvider(this).get(HotSearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        setSupportActionBar(mToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_return)
        }
        val datas = arrayOf(
            "guojingbu shi",
            "baijun yu",
            "wei zeng",
            "zhao xue wei",
            "jin yong chang",
            "gaven",
            "zhu wen ling hen mei",
            "xiao wu",
            "yang",
            "guo"
        )
        val layoutInflater = LayoutInflater.from(this)
        for (i in datas.indices) {
            val textView = layoutInflater.inflate(R.layout.waterflow_layout_view, mWaterFlowLayout, false) as TextView
            textView.text = datas[i]
            mWaterFlowLayout.addView(textView)
        }
        viewModel.hotSearch()//获取热搜数据
        viewModel.hotSearchLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                viewModel.dataList.addAll(data)
                Log.d("zwy",viewModel.dataList.size.toString())
                initRecyclerView()
            }
        })

    }

    //内部使用的各个fun
    private fun init() {
        mToolbar = findViewById(R.id.search_too_bar)
        mRecyclerView=findViewById(R.id.rv_search_activity)
        mWaterFlowLayout=findViewById(R.id.wf_search)
    }
    private fun initRecyclerView(){
        //对recyclerView做一些初始化操作
        val layoutManager=LinearLayoutManager(this)
        mRecyclerView.layoutManager=layoutManager
        val adapter=HotDataRecyclerView(viewModel.dataList)

        mRecyclerView.adapter=adapter
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

        //对界面的数据进行保存
        val dataList = mutableListOf<Data>()

        //使用Transformations的switchMap()方法来观察这个对象，否则仓库层返回的LiveData对象将无法进行观察
        val hotSearchLiveData = Transformations.switchMap(hotSearchData) {
            Repository.hotSearch()
        }

        fun hotSearch() {
            hotSearchData.value = ""
        }

    }
}