package pl.polsl.project.catalogex.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import pl.polsl.project.catalogex.R


class CameraScreenChooseDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choose_photo, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window!!.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}