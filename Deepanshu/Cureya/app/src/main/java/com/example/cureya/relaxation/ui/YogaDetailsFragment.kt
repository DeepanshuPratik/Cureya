package com.example.cureya.relaxation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.example.cureya.databinding.FragmentRelaxationYogaDetailsBinding
import com.example.cureya.model.Yoga
import com.example.cureya.relaxation.ui.YogaFragment.Companion.YOGA_LIST
import com.example.cureya.relaxation.ui.YogaFragment.Companion.YOGA_TITLE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class YogaDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRelaxationYogaDetailsBinding
    private lateinit var db: FirebaseDatabase

    private val navArgument: YogaDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationYogaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database

        val yogaTitle = navArgument.itemTitle

        db.reference.child(YOGA_LIST).orderByChild(YOGA_TITLE).equalTo(yogaTitle)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val item = snapshot.children.first().getValue(Yoga::class.java)!!
                        bindData(item)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("YogaDetailsFragment", "List with yoga title does not exists", error.toException())
                }
            })
    }

    private fun bindData(item: Yoga) {
        var stepCount = 0
        val steps = item.steps!!.split('.')

        binding.yogaActionTitle.text = item.title
        binding.yogaDetailDescription.text = item.description
        binding.yogaImage.load(item.imgUrl)

        while (stepCount < steps.size - 1) {
            val str = "Step ${stepCount + 1}: ".plus(steps[stepCount] + ".\n\n")
            binding.yogaDetailSteps.append(str)
            stepCount++
        }
    }
}