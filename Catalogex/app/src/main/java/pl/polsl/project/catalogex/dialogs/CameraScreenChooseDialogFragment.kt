package pl.polsl.project.catalogex.dialogs

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import kotlinx.android.synthetic.main.dialog_choose_photo.*
import pl.polsl.project.catalogex.R
import android.graphics.BitmapFactory
import pl.polsl.project.catalogex.interfaces.ImageTakenInterface

//Klasa odpowiedzialna za obsługę okna dialogowego, wykorzystywanego do wyboru sposobu dodania obrazu
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "PrivatePropertyName")
class CameraScreenChooseDialogFragment : DialogFragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val RESULT_LOAD_IMG = 2
    private var packageManager: PackageManager? = null
    private var activity: Activity? = null

    fun setArguments(activity: Activity,packageManager: PackageManager ){
        this.activity = activity
        this.packageManager = packageManager
    }

    //Metoda jest wywoływana podczas tworzenia widoku
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choose_photo, container)
    }

    //Metoda jest wywoływana po tworzeniu widoku
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitButtonDialogPhotos.setOnClickListener{
            viewL -> dismiss()
        }

        cameraButtonOption.setOnClickListener{
            viewL -> dispatchTakePictureIntent()
        }

        galleryButtonOption.setOnClickListener{
            viewL -> dispatchGetFromGalleryPictureIntent()
        }
    }

    //Metoda rozpatrująca wykonanie zdjęcia
    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),REQUEST_IMAGE_CAPTURE)
        } else {
            makePhoto()
        }
    }

    //Metoda wykonująca zdjęcie
    private fun makePhoto(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    //Metoda rozpatrująca wybranie zdjęcia z galerii
    private fun dispatchGetFromGalleryPictureIntent() {

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),RESULT_LOAD_IMG)
        } else {
            chooseFromGallery()
        }

    }

    //Metoda wybierająca zdjęcie z galerii
    private fun chooseFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    //Metoda rozpatrująca wyniki otrzymane w skutek uruchomienia innego ekranu
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data!!.extras.get("data") as Bitmap
            (activity!! as ImageTakenInterface).imageHasBeenTaken(imageBitmap)
            dismiss()

        }else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {

            val imageUri = data!!.data
            val imageStream = activity!!.contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            (activity!! as ImageTakenInterface).imageHasBeenTaken(selectedImage)
            dismiss()
        }

    }

    //Metoda rozpatrująca wyniki otrzymane w skutek decyzji dotyczącej uprawnień
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            makePhoto()

        if(requestCode == RESULT_LOAD_IMG && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            chooseFromGallery()
    }
}