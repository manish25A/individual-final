package com.manish.bhojmandu.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.manish.bhojmandu.R
import com.manish.bhojmandu.Utility.Notifications
import com.manish.bhojmandu.entity.CartItem
import com.manish.bhojmandu.repository.CartRepository
import com.manish.bhojmandu.ui.MainActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CartItemAdapter(
    val cartItemlist: ArrayList<CartItem>,
    val context: Context,
) : RecyclerView.Adapter<CartItemAdapter.ItemCartViewHolder>() {
    class ItemCartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView
        val price: TextView
        val deleteButton:Button
        val btnAdd:Button
        val btnMinus:Button
        val cartQuantity:TextView
        val checkOut:Button

        init {
            itemName = view?.findViewById(R.id.itemName)
            price = view?.findViewById(R.id.price)
            deleteButton=view?.findViewById(R.id.deleteFromCart)
            btnAdd=view?.findViewById(R.id.btn_add)
            btnMinus=view?.findViewById(R.id.btnMinus)
            cartQuantity=view?.findViewById(R.id.cart_quantity)
            checkOut=view?.findViewById(R.id.checkOut)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_items, parent, false)
        return ItemCartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemCartViewHolder, position: Int) {

        val cartItems = cartItemlist[position]
        holder.itemName.text = cartItems.itemId!![0].name
        holder.price.text = cartItems.itemId!![0].price
        holder.btnAdd.setOnClickListener(View.OnClickListener {
            var qty = holder.cartQuantity.text.toString().toInt()
            if (qty >= 10) {
                Toasty.warning(context, "Maximum quantity reached", Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                qty += 1
                holder.cartQuantity.setText(qty.toString())
                val totalPrice: Int = cartItems.itemId[0].price!!.toInt() * qty
                holder.price.text = totalPrice.toString()

            }
        })
        holder.btnMinus.setOnClickListener(View.OnClickListener {
            var qty = holder.cartQuantity.text.toString().toInt()
            if (qty <= 1) {
                Toasty.warning(context, "Minimum quantity reached", Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                qty -= 1
                holder.cartQuantity.setText(qty.toString())
                val totalPrice: Int = cartItems.itemId[0].price!!.toInt() * qty
                holder.price.text = totalPrice.toString()
            }
        })
        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setTitle("Delete cart Item")
            builder.setMessage("Are you sure to delete this Item?")
            builder.setIcon(R.drawable.ic_baseline_delete_24)
            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val cartItemDelete = CartRepository()
                            val response = cartItemDelete.deleteCart(cartItems._id!!)
                            if (response.success == true) {
                                withContext(Dispatchers.Main) {

                                    cartItemlist.remove(cartItems)
                                    notifyDataSetChanged()
                                    val notificationManager =
                                        NotificationManagerCompat.from(context)
                                    val notificationChannels = Notifications(context)
                                    notificationChannels.createNotificationChannels()

                                    val notification = NotificationCompat.Builder(
                                        context,
                                        notificationChannels.CHANNEL_2
                                    )
                                        .setSmallIcon(R.drawable.ic_heart_shape_outline)
                                        .setContentTitle("Item Deleted")
                                        .setContentText("Item Deleted Successfully from cart")
                                        .setColor(Color.BLUE)
                                        .build()
                                    notificationManager.notify(2, notification)
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT)
                                        .show()

                                }
                            }
                        } catch (ex: IOException) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    notifyItemRangeChanged(position, itemCount)
                    cartItemlist.removeAt(position)
                })
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, _ -> //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel()
                })
            val alert: AlertDialog = builder.create()
            alert.setCancelable(false)
            alert.show()

//            Toast.makeText(context,"$position",Toast.LENGTH_SHORT).show()
        }



        //checkout delete
        holder.checkOut.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setTitle("Proceed")
            builder.setMessage("Purchase confirmation")
            builder.setIcon(R.drawable.ic_shopping_cart)
            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val cartItemDelete = CartRepository()
                            val response = cartItemDelete.deleteCart(cartItems._id!!)
                            if (response.success == true) {
                                withContext(Dispatchers.Main) {

                                    cartItemlist.remove(cartItems)
                                    notifyDataSetChanged()
                                    val notificationManager = NotificationManagerCompat.from(context)
                                    val notificationChannels = Notifications(context)
                                    notificationChannels.createNotificationChannels()

                                    val notification = NotificationCompat.Builder(context,notificationChannels.CHANNEL_1)
                                        .setSmallIcon(R.drawable.logo1)
                                        .setContentTitle("Checkout Cart ")
                                        .setContentText("Checkout Successful. Your total price is ${holder.price.text}")
                                        .setColor(Color.BLUE)
                                        .build()
                                    notificationManager.notify(1,notification)
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()

                                }
                            }
                        } catch (ex: IOException) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    notifyItemRangeChanged(position, itemCount)
                    cartItemlist.removeAt(position)
                })
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, _ -> //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel()
                })
            val alert: AlertDialog = builder.create()
            alert.setCancelable(false)
            alert.show()
//            Toast.makeText(context,"$position",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return cartItemlist.size
    }

}