package pl.polsl.project.catalogex.dialogs

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_create_template_screen.*
import kotlinx.android.synthetic.main.dialog_choose_photo.*
import pl.polsl.project.catalogex.R
import android.widget.Toast
import android.graphics.BitmapFactory
import android.R.attr.data
import java.io.FileNotFoundException


class CameraScreenChooseDialogFragment : DialogFragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    val RESULT_LOAD_IMG = 2
    var packageManager: PackageManager? = null
    var image:ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choose_photo, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitButtonDialogPhotos.setOnClickListener{
            view -> dismiss()
        }

        cameraButtonOption.setOnClickListener{
            view -> dispatchTakePictureIntent()
        }

        galleryButtonOption.setOnClickListener{
            view -> dispatchGetFromGalleryPictureIntent()
        }

    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),REQUEST_IMAGE_CAPTURE)
        } else {
            makePhoto()
        }
    }

    private fun makePhoto(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun dispatchGetFromGalleryPictureIntent() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),RESULT_LOAD_IMG)
        } else {
            chooseFromGallery()
        }

    }

    private fun chooseFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            image!!.setImageBitmap(imageBitmap)
            image!!.scaleType = ImageView.ScaleType.FIT_CENTER

        }else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {

            val imageUri = data!!.data
            val imageStream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            image!!.setImageBitmap(selectedImage)
            image!!.scaleType = ImageView.ScaleType.FIT_CENTER

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            makePhoto()
        if(requestCode == RESULT_LOAD_IMG && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            chooseFromGallery()
    }
}