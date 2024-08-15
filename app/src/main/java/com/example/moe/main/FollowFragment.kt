package com.example.moe.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moe.DetailActivity
import com.example.moe.HomeFragment
import com.example.moe.MainActivity
import com.example.moe.PageRVAdapter
import com.example.moe.R
import com.example.moe.Search
import com.example.moe.SearchActivity
import com.example.moe.SearchRVAdapter
import com.example.moe.SearchViewModel
import com.example.moe.databinding.FragmentFollowBinding
import com.example.moe.databinding.ItemFollowBinding

class FollowFragment(private val viewModel: SearchViewModel) : Fragment() {
    private lateinit var binding : FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater,container,false)

        val recyclerview = binding.followRv

        val itemlist = ArrayList<FollowItem>()
        itemlist.add(FollowItem("기안도1","12.13"))
        itemlist.add(FollowItem("기안도2","12.13"))
        itemlist.add(FollowItem("기안도3","12.13"))
        itemlist.add(FollowItem("기안도4","12.13"))

        val followadapter = FollowRecyclerAdapter(itemlist)
        followadapter.notifyDataSetChanged()


        recyclerview.adapter = followadapter
        recyclerview.layoutManager = GridLayoutManager(context,2)

        binding.followChangemode.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_frm, FollowCardFragment()).commit()
        }

        var isnew = 0
        var isold = 0
        binding.followBtn1.setOnClickListener {
            if(isnew == 0 && isold == 0) {
                isnew = 1
                binding.followBtn1.setImageResource(R.drawable.follow_btn1_after)
            }
            else if (isold == 1) {
                isold = 0
                isnew = 1
                binding.followBtn2.setImageResource(R.drawable.follow_btn2)
                binding.followBtn1.setImageResource(R.drawable.follow_btn1_after)
            }
            else if (isnew == 1 && isold == 0) {
                isnew = 0
                binding.followBtn1.setImageResource(R.drawable.follow_btn1)
            }
        }
        binding.followBtn2.setOnClickListener {
            if(isnew == 0 && isold == 0) {
                isold = 1
                binding.followBtn2.setImageResource(R.drawable.follow_btn2_after)
            }
            else if (isnew == 1) {
                isnew = 0
                isold = 1
                binding.followBtn1.setImageResource(R.drawable.follow_btn1)
                binding.followBtn2.setImageResource(R.drawable.follow_btn2_after)
            }
            else if (isold == 1) {
                isold = 0
                binding.followBtn2.setImageResource(R.drawable.follow_btn2)
            }
        }
        

        //뒤로가기 클릭시
        binding.followBackbtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }

        //마이페이지 버튼 클릭시
        binding.followMypagebtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_mypage
        }

        //검색버튼 클릭시
        binding.followSearchbtn.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }




        return binding.root

    }

    override fun onStart() {
        super.onStart()
        getResult()
    }

    private fun getResult(){
        viewModel.searchState.observe(this){
            when{
                it.loading ->{
                    binding.followPagingLl.visibility = View.GONE
                }
                it.error != null ->{
                    binding.followPagingLl.visibility = View.GONE
                    Log.d("error", it.error)
                }
                else ->{
                    if (it.list != null) {
                        initRecyclerView(it.list)
                        setPageIndex(it.list)
                        binding.followPagingLl.visibility = View.VISIBLE
                    } else{
                        binding.followPagingLl.visibility = View.GONE
                        Log.d("result", "no result")
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(list: List<Search>) {
        val searchAdapter = SearchRVAdapter(list, viewModel)
        viewModel.pageIndex.observe(this) {
            searchAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPageIndex(list: List<Search>){
        val resultCount = list.size
        val totalPages = if (resultCount % 6 == 0) resultCount / 6 else resultCount / 6 + 1
        val pageIndexList = splitIntoChunks(totalPages)
        val pageAdapter = PageRVAdapter(pageIndexList, viewModel)
        binding.followPageRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.followPageRv.adapter = pageAdapter

        binding.followPageNextBtn.setOnClickListener {
            if(viewModel.pageIndex.value!! < totalPages){
                if (viewModel.pageIndex.value!! % 3 == 0){
                    viewModel.increaseDisplayIndex()
                    viewModel.increaseOffset()
                    viewModel.increaseIndex()
                }else{
                    viewModel.increaseIndex()
                }
            }
        }

        binding.followPagePrevBtn.setOnClickListener {
            if(viewModel.pageIndex.value!! > 1){
                if (viewModel.pageIndex.value!! % 3 == 1){
                    viewModel.decreaseDisplayIndex()
                    viewModel.decreaseOffset()
                    viewModel.decreaseIndex()
                }else{
                    viewModel.decreaseIndex()
                }
            }
        }

        viewModel.pageIndex.observe(this) {
            pageAdapter.notifyDataSetChanged()
        }
        viewModel.displayIndex.observe(this){
            pageAdapter.notifyDataSetChanged()
        }
        viewModel.offset.observe(this){
            pageAdapter.notifyDataSetChanged()
        }
    }

    private fun splitIntoChunks(number: Int): List<Int> {
        val result = mutableListOf<Int>()
        var remaining = number

        while (remaining > 0) {
            if (remaining >= 3) {
                result.add(3)
            } else {
                result.add(remaining)
            }
            remaining -= 3
        }

        return result
    }

}

class FollowRecyclerAdapter (
    var FollowList : MutableList<FollowItem>,
) : RecyclerView.Adapter<FollowRecyclerAdapter.FollowViewHolder>() {

    inner class FollowViewHolder(val binding : ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        val image : ImageView
        val title : TextView
        val heart : ImageView
        val date : TextView

        init {
            image = itemView.findViewById(R.id.follow_item_image)
            title = itemView.findViewById(R.id.follow_item_title)
            heart = itemView.findViewById(R.id.follow_item_heart)
            date = itemView.findViewById(R.id.follow_item_date)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow,parent, false)
        return FollowViewHolder(ItemFollowBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {

        holder.title.text = FollowList.get(position).title

        holder.date.text = FollowList.get(position).date

        var isheart = false
        holder.binding.followItemHeart.setOnClickListener {
            isheart = !isheart
            if(isheart) {
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart)
            } else {
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart_after)
            }
        }
    }

    override fun getItemCount(): Int {
        return FollowList.size
    }

}

class FollowItem (val title : String, val date : String)
