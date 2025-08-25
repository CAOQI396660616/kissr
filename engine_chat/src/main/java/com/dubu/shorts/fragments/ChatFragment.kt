package com.dubu.shorts.fragments

import android.os.Bundle
import android.view.View
import com.dubu.chat.R
import com.dubu.chat.databinding.FragmentChatBinding
import com.dubu.common.base.BaseBindingFragment


class ChatFragment : BaseBindingFragment<FragmentChatBinding>() {

    companion object {
        const val TAG = "ChatFragment"
        private const val DEFAULT_INDEX = "DEFAULT_INDEX"
        fun newInstance(index: Int = 0) = ChatFragment().also {
            it.arguments = Bundle().apply {
                putInt(DEFAULT_INDEX, index)
            }
        }
    }


    override fun getRootViewId(): Int {
        return R.layout.fragment_chat
    }


    override fun onViewCreated(root: View) {

    }

}