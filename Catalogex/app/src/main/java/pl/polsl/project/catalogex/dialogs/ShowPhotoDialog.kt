package pl.polsl.project.catalogex.dialogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.view.*
import kotlinx.android.synthetic.main.dialog_show_photo.*
import pl.polsl.project.catalogex.R

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ShowPhotoDialog : DialogFragment() {

    private var image: Bitmap? = null
    private var windowManager: WindowManager? = null

    fun setArguments(windowManager: WindowManager, image: Bitmap?){
        this.windowManager = windowManager
        this.image = image
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_show_photo, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val display = windowManager!!.defaultDisplay
        val size = Point()
        display.getSize(size)

        var value = (size.x*0.90).toInt()

        if(size.x > size.y){
            value = (size.y*0.90).toInt()
        }

        photoField.layoutParams = ConstraintLayout.LayoutParams(value, value)

        if(image!=null){
            photoField.setImageBitmap(image!!)
        }else {
            photoField.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.image))
        }

        exitButtonDialogPhotos.setOnClickListener{
            viewL ->
            dismiss()
        }

        photoField.setOnClickListener{
            viewL ->
            dismiss()
        }

    }

}