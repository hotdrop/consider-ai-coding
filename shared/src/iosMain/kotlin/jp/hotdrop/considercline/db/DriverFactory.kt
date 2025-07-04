package jp.hotdrop.considercline.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ConsiderClineDatabase.Schema,
            name = "considercline.db"
        )
    }
}
