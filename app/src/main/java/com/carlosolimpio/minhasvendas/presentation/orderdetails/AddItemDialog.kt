package com.carlosolimpio.minhasvendas.presentation.orderdetails

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.LayoutAddItemDialogBinding
import com.carlosolimpio.minhasvendas.domain.order.Item
import com.carlosolimpio.minhasvendas.domain.order.computeTotalValue
import com.carlosolimpio.minhasvendas.presentation.extensions.hideKeyboard
import com.carlosolimpio.minhasvendas.presentation.extensions.onDone
import com.carlosolimpio.minhasvendas.presentation.extensions.toBRLCurrencyString
import com.carlosolimpio.minhasvendas.presentation.extensions.toNumber

class AddItemDialog(
    private val editItem: Item?,
    private val onAddItemClick: (item: Item) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = LayoutAddItemDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
            .setTitle(getString(R.string.register_item))
            .setCancelable(true)
            .create()

        dialogBinding.apply {
            editTextItemName.setText(editItem?.name ?: "")
            editTextQuantity.setText(editItem?.count?.toString() ?: "")
            editTextUnitaryValue.setText(editItem?.value?.toString() ?: "")
            textItemTotalValue.text = getString(
                R.string.reais_value,
                editItem?.computeTotalValue()?.toString() ?: getString(R.string.number_zero)
            )

            editTextUnitaryValue.onDone {
                val total = editTextQuantity.text.toNumber() * editTextUnitaryValue.text.toNumber()
                textItemTotalValue.text = getString(R.string.reais_value, total.toBRLCurrencyString())
                editTextUnitaryValue.hideKeyboard(requireContext())
            }

            buttonRegisterItem.setOnClickListener {
                onAddItemClick(
                    Item(
                        name = dialogBinding.editTextItemName.text.toString(),
                        count = dialogBinding.editTextQuantity.text.toString().toInt(),
                        value = dialogBinding.editTextUnitaryValue.text.toString().toDouble()
                    )
                )
                dialog.dismiss()
            }

            buttonCancelItem.setOnClickListener { dialog.dismiss() }
        }

        return dialog
    }

    companion object {
        const val TAG = "AddItemDialog"
    }
}
