package ua.onpu.mindunlocker.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import ua.onpu.mindunlocker.LockActivity
import ua.onpu.mindunlocker.global.LockState

class AppLockService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            val prefs = getSharedPreferences("locked_apps", Context.MODE_PRIVATE)
            val lockedApps = prefs.getStringSet("locked", emptySet()) ?: emptySet()

            if (lockedApps.contains(packageName)) {
                if (LockState.lastUnlockedPackage != packageName) {
                    LockState.lastUnlockedPackage = null

                    val intent = Intent(this, LockActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra("PACKAGE_NAME", packageName)
                    }
                    startActivity(intent)
                }
            }
            else if (LockState.lastUnlockedPackage != null) {
                LockState.lastUnlockedPackage = null
            }

        }
    }

    override fun onInterrupt() {}
}
