package com.zhbcompany.retirementcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(
            application, "fd04bdc7-2aa2-4b6d-abeb-49e66df63a16",
            Analytics::class.java, Crashes::class.java
        )

        val calculateButton: Button = findViewById(R.id.calculateButton)
        val interestEditText: EditText = findViewById(R.id.interestEditText)
        val ageEditText: EditText = findViewById(R.id.ageEditText)
        val retirementEditText: EditText = findViewById(R.id.retirementEditText)
        val monthlySavingsEditText: EditText = findViewById(R.id.monthlySavingsEditText)
        val currentEditText: EditText = findViewById(R.id.currentEditText)

        calculateButton.setOnClickListener {
            try {
                val interestRate = interestEditText.text.toString().toFloat()
                val currentAge = ageEditText.text.toString().toInt()
                val retirementAge = retirementEditText.text.toString().toInt()
                val monthly = monthlySavingsEditText.text.toString().toFloat()
                val current = currentEditText.text.toString().toFloat()

                val properties: HashMap<String, String> = HashMap()
                properties["interest_rate"] = interestRate.toString()
                properties["current_age"] = currentAge.toString()
                properties["retirement_age"] = retirementAge.toString()
                properties["monthly_savings"] = monthly.toString()
                properties["current_savings"] = current.toString()

                if (interestRate <= 0) {
                    Analytics.trackEvent("wrong_interest_rate", properties)
                }
                if (retirementAge <= currentAge) {
                    Analytics.trackEvent("wrong_age", properties)
                }
            } catch (ex: Exception) {
                Analytics.trackEvent(ex.message)
            }
        }
    }
}