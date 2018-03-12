package com.euna.chordr

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import kotlin.collections.ArrayList


/**
 * Progression Bar fragment of the LoginActivity
 */
class ProgressionManager : Fragment(), BarAdapter.BarAdapterInterface {


    var progressionBar: RecyclerView? = null
    var barAdapter: BarAdapter? = null

    internal var chordsToBeConvertedToBars = LinkedList<ChordData>()

    private var mListener: ProgressionFragmentInteractionListener? = null

    interface ProgressionFragmentInteractionListener {
        fun onBarRemoved(pos: Int)
        fun onBarClicked(pos: Int)
        fun onBarLongClicked(pos: Int)
    }


    var adapterList: ArrayList<Bar>? = ArrayList<Bar>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layout = inflater!!.inflate(R.layout.fragment_progression, container, false)
        progressionBar = layout.findViewById(R.id.rvProgressionBar) as RecyclerView

        barAdapter = BarAdapter(this, context , adapterList!!)
        progressionBar!!.adapter = barAdapter
        progressionBar!!.layoutManager = GridLayoutManager(context, 4)


        //RecyclerView OnTouchListener
        registerForContextMenu(progressionBar!!)
        val onTouchCallback = BarEvents(barAdapter!!)
        val onTouchCallbackHelper = ItemTouchHelper(onTouchCallback)
        onTouchCallbackHelper.attachToRecyclerView(progressionBar)

        return layout
    }

    override fun onResume() {
        super.onResume()
    }

    override fun barAdapterOnBarRemoved(pos: Int) {
        mListener!!.onBarRemoved(pos)
    }

    override fun barAdapterOnBarClicked(pos: Int) {
        mListener!!.onBarClicked(pos)
    }

    override fun barAdapterOnBarLongClicked(pos: Int) {
        mListener!!.onBarLongClicked(pos)
    }

    fun setChordsToBeConvertedToBars(chordsInProgression: LinkedList<ChordData>) {
        chordsToBeConvertedToBars = chordsInProgression
        convertChordDataToBarData()
    }

    /* Uses current ChordData to convert to Progression Bar "Bars"*/
    fun convertChordDataToBarData() {
        adapterList!!.clear()

        for (chord in chordsToBeConvertedToBars) {
            val newBar: Bar = Bar()
            //newBar.IconId = R.drawable.playbutton
            newBar.mainText = chord.concatChord
            adapterList!!.add(newBar)
        }
        if (barAdapter != null) barAdapter!!.notifyDataSetChanged()
    }


    fun updateIntervals(newIntervals: Array<String>) {
        barAdapter!!.updateBarIntervals(newIntervals)
    }




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProgressionFragmentInteractionListener) {
            mListener = context as ProgressionFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
