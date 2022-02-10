package com.example.cureya.relaxation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.CardYogaBinding
import com.example.cureya.databinding.FragmentRelaxationYogaBinding
import com.example.cureya.model.Yoga
import com.example.cureya.relaxation.viewholder.YogaViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class YogaFragment : Fragment() {

    private lateinit var adapter: FirebaseRecyclerAdapter<Yoga, YogaViewHolder>
    private lateinit var binding: FragmentRelaxationYogaBinding
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationYogaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database

        val yogaRef = db.reference.child(YOGA_LIST)
        val yogaList = FirebaseRecyclerOptions.Builder<Yoga>()
            .setQuery(yogaRef, Yoga::class.java)
            .build()

        adapter = object: FirebaseRecyclerAdapter<Yoga, YogaViewHolder>(yogaList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YogaViewHolder {
                val layoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_yoga, parent, false)
                return YogaViewHolder(
                    CardYogaBinding.bind(layoutView),
                    this@YogaFragment,
                    binding.progressBar
                )
            }
            override fun onBindViewHolder(holder: YogaViewHolder, position: Int, model: Yoga) {
                holder.bind(model)
            }
        }

        binding.yogaCardRecyclerView.adapter = adapter
        binding.yogaCardRecyclerView.itemAnimator = null
    }

    fun goToYogaDetailsFragment(itemTitle: String) = findNavController().navigate(
        YogaFragmentDirections.actionYogaFragmentToYogaDetailsFragment(
            itemTitle
        )
    )

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        const val YOGA_LIST = "yoga"
        const val YOGA_TITLE = "title"
    }
}