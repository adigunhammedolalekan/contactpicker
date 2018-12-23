package cp.lib.cp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import cp.lib.cp.models.Contact
import java.util.*

class FetchContactTask(val context: Context?, callback: (ArrayList<Contact>) -> Unit) : Runnable {

    /*
    * Callback to send result back to caller
    * */
    private val mCallback: (ArrayList<Contact>) -> Unit = callback

    /*
    * Application context to access contact database
    * */
    private val mContext = context?.applicationContext
    private val contactMap: HashMap<String, Contact> = hashMapOf()

    /*
    * Android handler to communicate with android MainThread
    * */
    val handler = Handler(Looper.getMainLooper())

    val CONTACT_PROJECTIONS = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

    val PHONE_NUMBER_PROJECTIONS = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

    override fun run() {

        val resolver = mContext?.contentResolver
        val result: ArrayList<Contact> = ArrayList<Contact>()

        val cursor = resolver?.query(ContactsContract.Contacts.CONTENT_URI, CONTACT_PROJECTIONS,
                null, null, CONTACT_PROJECTIONS[1] + " DESC")

        if (cursor?.moveToFirst()!!) {

            val idIndex = cursor.getColumnIndex(CONTACT_PROJECTIONS[0])
            val nameIdx = cursor.getColumnIndex(CONTACT_PROJECTIONS[1])
            val photoIndex = cursor.getColumnIndex(CONTACT_PROJECTIONS[2])

            do {

                val contact = Contact()
                contact.name = cursor.getString(nameIdx)
                contact.phone = cursor.getString(photoIndex)
                contactMap.put(cursor.getString(idIndex), contact)
            }while (cursor.moveToNext())
        }

        cursor.close()

        //Map contact with phone number
        val phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONE_NUMBER_PROJECTIONS,
                null, null, null)

        if (phoneCursor?.moveToFirst()!!) {

            val phoneNumberIndex = phoneCursor.getColumnIndex(PHONE_NUMBER_PROJECTIONS[0])
            val contactIdIndex = phoneCursor.getColumnIndex(PHONE_NUMBER_PROJECTIONS[1])

            do {

                val contact = contactMap.get(phoneCursor.getString(contactIdIndex)) ?: continue
                contact.phone = phoneCursor.getString(phoneNumberIndex)

            }while (phoneCursor.moveToNext())
        }

        phoneCursor.close()

        contactMap.forEach { (_, contact) ->
            result.add(contact)
        }

        //Deliver results
        handler.post {

            mCallback.invoke(result)
        }
    }
}