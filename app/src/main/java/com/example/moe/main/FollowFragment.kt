package com.example.moe.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.SearchActivity
import com.example.moe.databinding.FragmentFollowBinding
import com.example.moe.databinding.ItemFollowBinding

class FollowFragment : Fragment() {
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

        var changemode = false
        binding.followChangemode.setOnClickListener {
            changemode = !changemode
            if(changemode){
                //카드모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode_after)
            } else {
                //기본모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode)
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





        return binding.root

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
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart_after)
            } else {
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart)
            }
        }
    }

    override fun getItemCount(): Int {
        return FollowList.size
    }

}

class FollowItem (val title : String, val date : String)
