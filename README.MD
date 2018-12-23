## ContactPicker
Beautiful, lightweight contact picker for android

![Contact List](https://github.com/adigunhammedolalekan/contactpicker/blob/master/art/s2.png =720x1280)

![Filtered](https://github.com/adigunhammedolalekan/contactpicker/blob/master/art/s1.png =720x1280)


### Why?
Because a custom contact picker would allow us create a similar user experience accross all android versions.

### Features
* Fast and Responsive - contacts are fetched on a dedicated backgroud thread
* Contact filter - filter contacts by `name` and `phone number` and receive result as you type
* Easy to use - Launch with a single line of code

### How to Use

* Clone repo
* Add module `cp` to your project `implementation project(":cp")`
* Declare `ContactPickerActivity.kt` in your `AndroidManifest.xml`
* Add `READ_CONTACTS` Permission to your `AndroidManifest.xml`
* Call `ContactPicker.open(activity)` from your `Activity/Fragment`


```Kotlin
//From activity
class MainActivity : AppCompactActivity() {

	override fun onCreate() {
		super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        ContactPicker.open(this) //or ContactPicker.open(this, 10) to use custom REQUEST_CODE
	}

	//
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ContactPickerActivity.RC_CONTACT_PICKER) {

            val result = data?.getParcelableExtra<Contact>(ContactPickerActivity.EXTRA_CONTACT_DATA)
            Log.d("Picker", result?.name + " " + result?.phone + " " + result?.photo)
        }
        
    }
}
```

```Kotlin

//From fragment
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
```

NOTE: Make sure you specify `ContactPickerActivity.java` theme inside your `AndroidManifest.xml` to be a `NoActionbar` theme, e.g 

`
<activity android:name="cp.lib.cp.ui.ContactPickerActivity" android:theme="@style/AppTheme.NoActionBar"/>
`

Where `AppTheme.NoAction` is defined like this

```xml

	<style name="AppTheme.NoActionBar" parent="AppTheme">
        <!-- Customize your theme here. -->
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
    </style>

    <style name="AppTheme" parent="@style/Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```

#### TODO
* Add multi picker feature

-:)
$_