package cp.lib.cp.models

import android.os.Parcel
import android.os.Parcelable

class Contact() : Parcelable {

    public var name: String? = ""
    public var phone: String? = ""
    public var photo: String? = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        phone = parcel.readString()
        photo = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}