package com.utesocial.android.feature_create_post.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentCreatePostBinding
import com.utesocial.android.feature_create_post.domain.model.MediaReq
import com.utesocial.android.feature_create_post.presentation.adapter.ChooseMediaAdapter
import com.utesocial.android.feature_create_post.presentation.element.partial.CreatePostFragmentInfo

class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {

    override lateinit var binding: FragmentCreatePostBinding
    override val viewModel: ViewModel? = null

    private val infoBinding by lazy { CreatePostFragmentInfo(binding.info) }

    private val data: ArrayList<MediaReq> by lazy { ArrayList() }
    private val chooseMediaAdapter by lazy { ChooseMediaAdapter(this@CreatePostFragment, data) }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCreatePostBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMedia.adapter = chooseMediaAdapter
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMedia)
    }

    private fun setupListener() {
        infoBinding.setupListener(getBaseActivity())

        binding.buttonAddMedia.setOnClickListener { chooseMultipleMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        ) }
    }

    private val chooseMultipleMediaLauncher = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri ->
        if (uri.isNotEmpty()) {
            val mediaReq: List<MediaReq> = uri.map {
                val isVideo = getBaseActivity().contentResolver.getType(it)!!.startsWith("video")
                MediaReq(uri = it, isVideo = isVideo)
            }
            val positionInsert = data.size

            data.addAll(mediaReq)
            chooseMediaAdapter.notifyItemInserted(positionInsert)
        }
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) {
            getBaseActivity().showSnackbar(getString(R.string.str_remove_item_create_post))
            val positionRemoved = viewHolder.adapterPosition

            data.removeAt(positionRemoved)
            chooseMediaAdapter.notifyItemRemoved(positionRemoved)
        }
    }
}