package pw.cub3d.commons.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context

object Accounts {
    // The library user should handle this permission requirement
    @SuppressLint("MissingPermission")
    fun getUserAccountsByType(type: String): Array<Account> {
        return accountManager!!.getAccountsByType(type)
    }

    fun hasUserAccountsWithType(type: String): Boolean {
        return getUserAccountsByType(type).isNotEmpty()
    }


    @SuppressLint("MissingPermission")
    fun removeAccount(account: Account) {
        accountManager!!.removeAccount(account, null, null, null)
    }


    private var accountManager: AccountManager? = null

    fun initialise(ctx: Context) {
        accountManager = AccountManager.get(ctx)
    }
}

fun Account.remove() {
    Accounts.removeAccount(this)
}