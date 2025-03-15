package jp.hotdrop.considercline.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ConsiderClineDatabase {
    val driver = driverFactory.createDriver()
    return ConsiderClineDatabase(driver)
}
