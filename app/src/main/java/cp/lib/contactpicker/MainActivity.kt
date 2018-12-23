package cp.lib.contactpicker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cp.lib.cp.ContactPicker
import cp.lib.cp.models.Contact
import cp.lib.cp.ui.ContactPickerActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        ContactPicker.open(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ContactPickerActivity.RC_CONTACT_PICKER) {

            val result = data?.getParcelableExtra<Contact>(ContactPickerActivity.EXTRA_CONTACT_DATA)
            Log.d("Picker", result?.name + " " + result?.phone + " " + result?.photo)
        }

    }
}