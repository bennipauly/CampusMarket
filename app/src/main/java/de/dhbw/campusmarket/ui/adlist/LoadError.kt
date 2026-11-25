package de.dhbw.campusmarket.ui.adlist

// Why loading the list failed. Every case gets its own message on the screen.
// "sealed": these are all cases there are, so a "when" over LoadError needs no else branch.
sealed interface LoadError {

    // IOException: no internet, airplane mode, server not reachable.
    data object NoInternet : LoadError

    // HttpException: the server answered, but with an error code (e.g. 404 or 500).
    data class Server(val code: Int) : LoadError

    // SerializationException: the JSON does not fit our model (missing field, unknown category, ...).
    data object InvalidData : LoadError

    // SQLiteException: reading or writing the local database failed.
    data object Database : LoadError
}
