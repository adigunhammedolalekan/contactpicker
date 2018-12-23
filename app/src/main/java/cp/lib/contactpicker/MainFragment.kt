package cp.lib.contactpicker

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cp.lib.cp.ContactPicker
import cp.lib.cp.models.Contact
import cp.lib.cp.ui.ContactPickerActivity

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ContactPicker.open(activity!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ContactPickerActivity.RC_CONTACT_PICKER) {

            val result = data?.getParcelableExtra<Contact>(ContactPickerActivity.EXTRA_CONTACT_DATA)
            Log.d("Picker", result?.name + " " + result?.phone + " " + result?.photo)
        }

    }
}