package com.example.pokemonwiki.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonwiki.databinding.PartLoadStateLayoutBinding

/**
 * Action to be executed when Try Again button is pressed
 */
typealias HolderAction = () -> Unit

/**
 * This adapter is used for rendering the load state (ProgressBar, error message and Try Again button)
 * in the list's header and footer.
 */
class LoadInfoStateAdapter(
    private val tryAgainAction: HolderAction,
    private val switchOfflineModeAction: HolderAction
) : LoadStateAdapter<LoadInfoStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PartLoadStateLayoutBinding.inflate(inflater, parent, false)
        return Holder(binding, tryAgainAction, switchOfflineModeAction)
    }


    class Holder(
        private val binding: PartLoadStateLayoutBinding,
        private val tryAgainAction: HolderAction,
        private val switchOfflineModeAction: HolderAction
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tryAgainButton.setOnClickListener { tryAgainAction() }
            binding.SwitchToOfflineModeButton.setOnClickListener { switchOfflineModeAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            messageTextView.isVisible = loadState is LoadState.Error
            SwitchToOfflineModeButton.isVisible = loadState is LoadState.Error
            tryAgainButton.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }

}