package se.magictechnology.piaxroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MinAdapter() : RecyclerView.Adapter<HappyViewHolder>() {
    var firstnames = mutableListOf<String>()
    var lastnames = mutableListOf<String>()
    var ages = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyViewHolder {
        val vh = HappyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rad_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return firstnames.size
    }

    override fun onBindViewHolder(holder: HappyViewHolder, position: Int){
        holder.firstNameTV.text = firstnames[position]
        holder.lastNameTV.text = lastnames[position]
        holder.ageTV.text = ages[position]
    }

}

class HappyViewHolder (view: View) : RecyclerView.ViewHolder(view){
    var firstNameTV = view.findViewById<TextView>(R.id.firstNameTV)
    var lastNameTV = view.findViewById<TextView>(R.id.lastNameTV)
    var ageTV = view.findViewById<TextView>(R.id.ageTV)
}