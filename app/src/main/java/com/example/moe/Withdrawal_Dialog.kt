package com.example.moe

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.moe.databinding.DialogWithdrawalBinding

class Withdrawal_Dialog (context: Context, Interface: WithdrawalDialogInterface) : Dialog(context) {

    // 액티비티에서 인터페이스를 받아옴
    private var customDialogInterface: WithdrawalDialogInterface = Interface

    private lateinit var addButton : TextView
    private lateinit var cancelButton : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_withdrawal)

        addButton = findViewById(R.id.dialog_yesbtn)
        cancelButton = findViewById(R.id.dialog_nobtn)

        // 배경을 투명하게함
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 추가 버튼 클릭 시 onAddButtonClicked 호출 후 종료
        addButton.setOnClickListener {
            customDialogInterface.onAddButtonClicked()
            dismiss()}

        // 취소 버튼 클릭 시 onCancelButtonClicked 호출 후 종료
        cancelButton.setOnClickListener {
            customDialogInterface.onCancelButtonClicked()
            dismiss()}
    }
}