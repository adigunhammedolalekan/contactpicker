package cp.lib.cp

import android.app.Activity
import android.content.Intent
import cp.lib.cp.ui.ContactPickerActivity

object ContactPicker {

    public fun open(context: Activity, code: Int) {
        val intent = Intent(context, ContactPickerActivity::class.java)
        context.startActivityForResult(intent, code)
    }

    public fun open(context: Activity) {
        open(context, ContactPickerActivity.RC_CONTACT_PICKER)
    }

}