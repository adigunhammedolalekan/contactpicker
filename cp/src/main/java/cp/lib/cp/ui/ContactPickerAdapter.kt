package cp.lib.cp.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.FrameLayout
import android.widget.TextView
import cp.lib.cp.R
import cp.lib.cp.models.Contact
import de.hdodenhof.circleimageview.CircleImageView

class ContactPickerAdapter(val result: ArrayList<Contact>)
    : RecyclerView.Adapter<ContactPickerAdapter.ContactViewHolder>(), Filterable {

    private var data = result
    private val copy = result

    public var clickListener: (Contact) -> Unit = {_ ->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_contact,
                parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val contact = data[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phone

        holder.root.setOnClickListener {
            clickListener.invoke(contact)
        }
    }

    override fun getFilter() = namedFilter

    private val namedFilter: Filter = object: Filter() {

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

            if (p1?.values == null || p1.count < 0) {

                data.clear()
                data = copy
                notifyDataSetChanged()
                return
            }

            data = p1.values as (ArrayList<Contact>)
            notifyDataSetChanged()
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {

            return if (p0 == null || p0.toString().trim().isEmpty()) {

                val result = FilterResults()

                result.count = 0
                result.values = null
                result
            } else {
                val data = filterContacts(p0)
                val result = FilterResults()

                result.count = 0
                result.values = data
                result
            }
        }
    }

    private fun filterContacts(p0: CharSequence?): ArrayList<Contact> {

        val value = p0.toString().trim().toLowerCase()
        val response = ArrayList<Contact>()

        copy.forEach {

            if (it.name != null && it.phone != null) {

                if (it.name!!.toLowerCase().contains(value) || it.phone!!.toLowerCase().contains(value)) {
                    response.add(it)
                }
            }
        }

        return response
    }


    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val photo = view.findViewById<CircleImageView>(R.id.iv_contact_photo)
        val nameTextView = view.findViewById<TextView>(R.id.tv_contact_name)
        val phoneTextView = view.findViewById<TextView>(R.id.tv_contact_phone_number)
        val root = view.findViewById<FrameLayout>(R.id.layout_contact_root)
    }
}