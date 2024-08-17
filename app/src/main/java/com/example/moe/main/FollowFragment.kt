package com.example.moe.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.moe.R
import com.example.moe.databinding.FragmentFollowBinding
import com.example.moe.databinding.ItemFollowBinding
import com.example.moe.detail.follow.ui.FollowItemActivity
import com.example.moe.detail.search.ui.SearchActivity
import com.example.moe.main.MainAPI.FollowCardAdapter
import com.example.moe.main.MainAPI.FollowCardItem
import kotlin.math.abs

class FollowFragment : Fragment() {
    private lateinit var binding : FragmentFollowBinding
    private lateinit var items: List<FollowCardItem>
    private lateinit var adapter: FollowCardAdapter

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

        var changemode = false
        binding.followChangemode.setOnClickListener {
            changemode = !changemode
            if(changemode){
                //카드모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode_after)
                binding.followRv.visibility = View.GONE
                binding.cardModeContainer.visibility = View.VISIBLE
            } else {
                //기본모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode)
                binding.followRv.visibility = View.VISIBLE
                binding.cardModeContainer.visibility = View.GONE
            }
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

        //홈버튼 클릭시
        binding.followHomebtn.setOnClickListener {

        }

        //검색버튼 클릭시
        binding.followSearchbtn.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        dummyData()
        setUpTransformer()
        setPageChangeCallback()

        return binding.root

    }

    // CardMode : viewpager2 안에 아이템 위치 설정
    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(0))
        transformer.addTransformer { page, position ->

            val absPos = abs(position)

            page.alpha = when {
                absPos >= 3 -> 0f
                absPos >= 2 -> 1f
                absPos >= 1 -> 1f
                else -> 1f
            }

            page.translationZ = when {
                absPos >= 3 -> 0f
                absPos >= 2 -> 1f
                absPos >= 1 -> 2f
                else -> 3f
            }

            page.scaleY = 0.85f + (1 - absPos) * 0.15f
            page.translationX = -position * (page.width / 2.5f)

        }

        binding.viewPager2.setPageTransformer(transformer)
    }

    // CardMode: 아이템 이동 시 위치 업로드 + 아이템 제목과 날짜 업로드
    private fun setPageChangeCallback() {
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("PageChange", "Selected page: $position")
                val item = items[position % items.size]
                binding.cardModeTitle.text = item.title
                binding.cardModeDate.text = item.date
            }
        })
    }

    // CardMode: 더미 데이터 + 스크롤 방식 설정
    private fun dummyData(){

        // 더미 데이터
        items = listOf(
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 1", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 2", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 3", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 4", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 5", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 6", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 7", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 8", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 9", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 10", "2024.8.12 ~ 2024.8.12"),
            FollowCardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 11", "2024.8.12 ~ 2024.8.12")
        )

        // 아이템 클릭 시 액티비티로 데이터 전달
        adapter = FollowCardAdapter(items, binding.viewPager2) { item ->
            val intent = Intent(requireContext(), FollowItemActivity::class.java).apply {
                putExtra("IMAGE_URL", item.imageUrl)
                putExtra("TITLE", item.title)
                putExtra("DATE", item.date)
            }
            startActivity(intent)
        }

        binding.viewPager2.adapter = adapter
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewPager2.setCurrentItem(2000, false)
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
