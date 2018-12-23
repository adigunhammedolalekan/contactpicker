package cp.lib.cp.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import cp.lib.cp.FetchContactTask
import cp.lib.cp.R
import cp.lib.cp.models.Contact
import org.parceler.Parcels
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ContactPickerActivity : AppCompatActivity() {

    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    private val contactsData: ArrayList<Contact> = ArrayList<Contact>()
    private val adapter: ContactPickerAdapter = ContactPickerAdapter(contactsData)

    private var mRecyclerView: RecyclerView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout?= null
    private var mCloseButton: ImageView? = null
    private var mFilterEditText: EditText? = null

    companion object {

        const val EXTRA_CONTACT_DATA = "EXTRA_CONTACT_DATA"
        const val RC_CONTACT_PICKER = 12
        const val PERMISSION_REQUEST_CODE = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_picker_activity)

        setupViews()
        requestPermission()
    }

    private fun fetchContacts() {

        mSwipeRefreshLayout?.isRefreshing = true
        executor.execute(FetchContactTask(this) { data ->

            contactsData.clear()
            data.forEach {
                Log.d("CP", it.photo)
                contactsData.add(it)
            }

            mSwipeRefreshLayout?.isRefreshing = false
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupViews() {

        mRecyclerView = findViewById(R.id.rv_contacts)
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout_contact_picker)
        mCloseButton = findViewById(R.id.btn_close_picker)
        mFilterEditText = findViewById(R.id.edt_filter_)

        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.adapter = adapter

        adapter.clickListener = {

            val intent = intent
            intent.putExtra(EXTRA_CONTACT_DATA, it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        mSwipeRefreshLayout?.setOnRefreshListener {
            fetchContacts()
        }
        mCloseButton?.setOnClickListener {
            finish()
        }

        mFilterEditText?.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun requestPermission() {

        val state = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (state == PackageManager.PERMISSION_GRANTED) {

            fetchContacts()
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                fetchContacts()
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        executor.shutdownNow()
    }
}