package com.zhbcompany.retirementcalculator

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(
            application, "b4f1ca28-165b-46a6-b1ad-10491c4a9c7b",
            Analytics::class.java, Crashes::class.java
        )

        val calculateButton: Button = findViewById(R.id.calculateButton)
        val interestEditText: EditText = findViewById(R.id.interestEditText)
        val ageEditText: EditText = findViewById(R.id.ageEditText)
        val retirementEditText: EditText = findViewById(R.id.retirementEditText)
        val monthlySavingsEditText: EditText = findViewById(R.id.monthlySavingsEditText)
        val currentEditText: EditText = findViewById(R.id.currentEditText)
        val resultTextView: TextView = findViewById(R.id.resultTextView)
        val typeBuild: TextView = findViewById(R.id.typeBuild)

        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        if (isDebuggable) {
            typeBuild.text = "Debug Version"
        } else {
            typeBuild.text = "Release Version"
        }

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

                val futureSavings = calculateRetirement(interestRate, current, monthly, (retirementAge - currentAge)*12)

                resultTextView.text = "At the current rate of $interestRate%, saving \$$monthly a month you will have \$${String.format("%f", futureSavings)} by $retirementAge."
            } catch (ex: Exception) {
                Analytics.trackEvent(ex.message)
            }
        }
    }

    fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float {
        var futureSavings = currentSavings * (1+(interestRate/100/12)).pow(numMonths)

        for (i in 1..numMonths) {
            futureSavings += monthly * (1+(interestRate/100/12)).pow(i)
        }

        return  futureSavings
    }
}
