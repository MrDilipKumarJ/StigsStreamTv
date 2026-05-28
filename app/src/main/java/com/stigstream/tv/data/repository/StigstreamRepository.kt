package com.stigstream.tv.data.repository

import com.stigstream.tv.data.model.ContentRow
import com.stigstream.tv.data.model.ContentType
import com.stigstream.tv.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * StigstreamRepository
 *
 * Fetches content from stigstream.com.
 * The app scrapes the public pages or calls the internal API if available.
 * For now, it uses TMDB-style poster URLs via stigstream content IDs.
 *
 * Replace BASE_URL / endpoints to match Stigstream's actual API once discovered.
 */
class StigstreamRepository {

    companion object {
        const val BASE_URL = "https://stigstream.com"
        const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
        const val TMDB_BACKDROP_BASE = "https://image.tmdb.org/t/p/original"
    }

    // ── Home Screen Rows ──────────────────────────────────────────────────────

    suspend fun getHomeRows(): List<ContentRow> = withContext(Dispatchers.IO) {
        listOf(
            ContentRow("featured",   "🔥 Featured",          getFeaturedMovies()),
            ContentRow("trending",   "📈 Trending Now",       getTrendingMovies()),
            ContentRow("new",        "🆕 New Releases",       getNewReleases()),
            ContentRow("action",     "💥 Action & Adventure", getByGenre("action")),
            ContentRow("comedy",     "😂 Comedy",             getByGenre("comedy")),
            ContentRow("horror",     "👻 Horror",             getByGenre("horror")),
            ContentRow("scifi",      "🚀 Sci-Fi",             getByGenre("sci-fi")),
            ContentRow("tvshows",    "📺 TV Shows",           getTvShows()),
            ContentRow("animated",   "🎨 Animated",           getByGenre("animation")),
            ContentRow("thriller",   "🔪 Thriller",           getByGenre("thriller")),
        )
    }

    // ── Movie Data (backed by real TMDB poster URLs + Stigstream stream links) ─

    private fun getFeaturedMovies(): List<Movie> = listOf(
        Movie("tt1375666", "Inception",          "$TMDB_IMAGE_BASE/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",   "$TMDB_BACKDROP_BASE/s3TBrRGB1iav7gFOCNx3H31MoES.jpg", "2010", "8.8", "Sci-Fi", "2h 28m", "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.", "$BASE_URL/watch/tt1375666", ContentType.MOVIE, "4K"),
        Movie("tt0468569", "The Dark Knight",    "$TMDB_IMAGE_BASE/qJ2tW6WMUDux911r6m7haRef0WH.jpg",  "$TMDB_BACKDROP_BASE/nMKdUUepR0i5zn0y1T4CejMPAZM.jpg", "2008", "9.0", "Action", "2h 32m", "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests.", "$BASE_URL/watch/tt0468569", ContentType.MOVIE, "4K"),
        Movie("tt0816692", "Interstellar",       "$TMDB_IMAGE_BASE/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",  "$TMDB_BACKDROP_BASE/pbrkL804c8yAv3zBZR4QPEafpAR.jpg", "2014", "8.6", "Sci-Fi", "2h 49m", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", "$BASE_URL/watch/tt0816692", ContentType.MOVIE, "4K"),
        Movie("tt4154796", "Avengers: Endgame",  "$TMDB_IMAGE_BASE/or06FN3Dka5tukK1e9sl16pB3iy.jpg",  "$TMDB_BACKDROP_BASE/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg", "2019", "8.4", "Action", "3h 1m",  "After the devastating events of Infinity War, the Avengers assemble once more to reverse Thanos's actions.", "$BASE_URL/watch/tt4154796", ContentType.MOVIE, "4K"),
        Movie("tt0137523", "Fight Club",         "$TMDB_IMAGE_BASE/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",  "$TMDB_BACKDROP_BASE/52AfXWuXCHn3UjD17rBruA9f5qb.jpg", "1999", "8.8", "Thriller","2h 19m", "An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much, much more.", "$BASE_URL/watch/tt0137523", ContentType.MOVIE, "HD"),
        Movie("tt0120737", "The Lord of the Rings: The Fellowship", "$TMDB_IMAGE_BASE/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg", "$TMDB_BACKDROP_BASE/pIgZO4Lhq2LgO04T7hMftnYLXoN.jpg", "2001", "8.8", "Fantasy", "3h 48m", "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring.", "$BASE_URL/watch/tt0120737", ContentType.MOVIE, "HD"),
    )

    private fun getTrendingMovies(): List<Movie> = listOf(
        Movie("tt15398776","Oppenheimer",        "$TMDB_IMAGE_BASE/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",  "$TMDB_BACKDROP_BASE/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg", "2023", "8.5", "Drama",  "3h",    "The story of J. Robert Oppenheimer's role in the development of the atomic bomb.", "$BASE_URL/watch/tt15398776", ContentType.MOVIE, "4K"),
        Movie("tt1517268", "Barbie",             "$TMDB_IMAGE_BASE/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",  "$TMDB_BACKDROP_BASE/nHf61UzkfFno5X1ofIhugCPus2R.jpg", "2023", "6.9", "Comedy", "1h 54m","Barbie suffers a crisis that leads her to question her world.", "$BASE_URL/watch/tt1517268", ContentType.MOVIE, "4K"),
        Movie("tt9362722", "Spider-Man: Across the Spider-Verse", "$TMDB_IMAGE_BASE/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", "$TMDB_BACKDROP_BASE/4HodYYKEIsGOdinkGi2Ucz6X9i0.jpg", "2023", "8.7", "Animation","2h 20m","Miles Morales catapults across the Multiverse.", "$BASE_URL/watch/tt9362722", ContentType.MOVIE, "4K"),
        Movie("tt6718170", "The Super Mario Bros. Movie", "$TMDB_IMAGE_BASE/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg", "$TMDB_BACKDROP_BASE/9n2tJBplPbgR2ca05hS5CKXwP2c.jpg", "2023", "7.1", "Animation","1h 32m","While working underground to fix a water main, Brooklyn plumbers Mario and Luigi are transported down a mysterious pipe.", "$BASE_URL/watch/tt6718170", ContentType.MOVIE, "HD"),
        Movie("tt10366460","Guardians of the Galaxy Vol. 3","$TMDB_IMAGE_BASE/r2J02Z6a5qTJl2mWF8tTv1TEBLA.jpg","$TMDB_BACKDROP_BASE/5YZbUmjbMa3ClvSW1Wj3D6XGkVA.jpg","2023","7.9","Action","2h 30m","Still reeling from the loss of Gamora, Peter Quill rallies his team on a mission to protect Rocket.","$BASE_URL/watch/tt10366460",ContentType.MOVIE,"HD"),
        Movie("tt5433140", "Fast X",             "$TMDB_IMAGE_BASE/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",  "$TMDB_BACKDROP_BASE/4XM8DUTQb3lhLemJC51Jx4a2EuA.jpg", "2023", "5.8", "Action", "2h 21m","Dom Toretto and his family are targeted by the vengeful son of drug kingpin Hernan Reyes.", "$BASE_URL/watch/tt5433140", ContentType.MOVIE, "HD"),
        Movie("tt2906216", "Dungeons & Dragons: Honor Among Thieves", "$TMDB_IMAGE_BASE/v7UF7ypAqjsFZFdjksjQ7IUpXdn.jpg","$TMDB_BACKDROP_BASE/eBU7gCjTCZlM7Xi5btFMcMY4oaq.jpg","2023","7.2","Fantasy","2h 14m","A charming thief and a band of unlikely adventurers embark on an epic quest.", "$BASE_URL/watch/tt2906216", ContentType.MOVIE, "HD"),
    )

    private fun getNewReleases(): List<Movie> = listOf(
        Movie("tt14230388","Aquaman and the Lost Kingdom","$TMDB_IMAGE_BASE/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg","$TMDB_BACKDROP_BASE/4bJeRTZR8sEpDKVhM9LAgeLIpWH.jpg","2023","5.4","Action","2h 4m","Black Manta seeks revenge on Aquaman for his father's death.", "$BASE_URL/watch/tt14230388", ContentType.MOVIE, "4K"),
        Movie("tt9603212", "Migration",          "$TMDB_IMAGE_BASE/ldfCF9RhR40mppkzmAfZRpxTbMO.jpg",  "$TMDB_BACKDROP_BASE/lB5S6G0bm7CMSMORTbnF5b5OYMK.jpg", "2023", "7.1", "Animation","1h 23m","A family of ducks try to convince their overprotective father to go on a migration.", "$BASE_URL/watch/tt9603212", ContentType.MOVIE, "HD"),
        Movie("tt21823606","Anyone But You",     "$TMDB_IMAGE_BASE/lurEK87kukWNaHd0zYnsi3yzJrs.jpg",  "$TMDB_BACKDROP_BASE/xs3bBdP6M0cX4P0oIYXaHNJBXsX.jpg", "2023", "6.8", "Romance", "1h 43m","After an amazing first date, Bea and Ben's initial attraction turns sour.", "$BASE_URL/watch/tt21823606", ContentType.MOVIE, "HD"),
        Movie("tt3480822", "Talk to Me",         "$TMDB_IMAGE_BASE/kdPMUi5Fgt4PZVrJjJ4AH3Fxgg4.jpg",  "$TMDB_BACKDROP_BASE/nH0kLWIBa5PaZAiSRiXM6JrxNJI.jpg", "2023", "7.1", "Horror",  "1h 35m","When a group of friends discover how to conjure spirits using an embalmed hand.", "$BASE_URL/watch/tt3480822", ContentType.MOVIE, "HD"),
        Movie("tt15239678","Dune: Part Two",     "$TMDB_IMAGE_BASE/czembW0Rk1Ke7lCJGahbOhdCuhV.jpg",  "$TMDB_BACKDROP_BASE/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg", "2024", "8.5", "Sci-Fi",  "2h 47m","Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators.", "$BASE_URL/watch/tt15239678", ContentType.MOVIE, "4K"),
        Movie("tt14916160","Deadpool & Wolverine","$TMDB_IMAGE_BASE/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg","$TMDB_BACKDROP_BASE/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg","2024","7.8","Action","2h 7m","Deadpool is recruited by the Time Variance Authority to work with Wolverine.", "$BASE_URL/watch/tt14916160", ContentType.MOVIE, "4K"),
    )

    private fun getTvShows(): List<Movie> = listOf(
        Movie("tt0944947", "Game of Thrones",    "$TMDB_IMAGE_BASE/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",  "$TMDB_BACKDROP_BASE/suopoADq0k8YZr4dQXcU6t2sCIH.jpg", "2011–2019", "9.2", "Drama",   "8 Seasons", "Nine noble families fight for control of the mythical land of Westeros.", "$BASE_URL/watch/show/tt0944947", ContentType.TV_SHOW, "4K"),
        Movie("tt0903747", "Breaking Bad",       "$TMDB_IMAGE_BASE/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",   "$TMDB_BACKDROP_BASE/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg", "2008–2013", "9.5", "Crime",   "5 Seasons", "A chemistry teacher turned methamphetamine producer.", "$BASE_URL/watch/show/tt0903747", ContentType.TV_SHOW, "HD"),
        Movie("tt7366338", "Chernobyl",          "$TMDB_IMAGE_BASE/hlLXt2tOPy1gX2C3nKD48HzEiLU.jpg",  "$TMDB_BACKDROP_BASE/eagMrtEPLvR3iUCFLhB9FbAuqtM.jpg", "2019", "9.4", "Drama",  "1 Season",  "In April 1986, an explosion at the Chernobyl nuclear power plant becomes one of the world's worst man-made catastrophes.", "$BASE_URL/watch/show/tt7366338", ContentType.TV_SHOW, "HD"),
        Movie("tt5491994", "Planet Earth II",    "$TMDB_IMAGE_BASE/9YjHGWJlKR2SWoULVtdMOLEA3Ca.jpg",  "$TMDB_BACKDROP_BASE/sWQ0BHg0toEeRwCE5UBRExW3hF4.jpg", "2016", "9.5", "Documentary","1 Season","Sir David Attenborough presents a documentary series about wildlife.", "$BASE_URL/watch/show/tt5491994", ContentType.TV_SHOW, "4K"),
        Movie("tt0455275", "Prison Break",       "$TMDB_IMAGE_BASE/5E1BhkCgjLBlqx557Z4bXKGDgEf.jpg",  "$TMDB_BACKDROP_BASE/zUZGSRSRGJy77fWPpSHhfMkMjK1.jpg", "2005–2017","8.3","Thriller","5 Seasons","A man deliberately gets himself incarcerated to help his innocent brother escape death row.", "$BASE_URL/watch/show/tt0455275", ContentType.TV_SHOW, "HD"),
        Movie("tt0773262", "Dexter",             "$TMDB_IMAGE_BASE/58H6Ctze1nnpS13UzGCHOHMKqPe.jpg",  "$TMDB_BACKDROP_BASE/6wgpPauvHdvOe52Bfa1xVmb7b7N.jpg", "2006–2013","8.6","Crime",  "8 Seasons","Dexter Morgan, a blood spatter pattern analyst for the Miami Metro Police also leads a secret life as a serial killer.", "$BASE_URL/watch/show/tt0773262", ContentType.TV_SHOW, "HD"),
        Movie("tt2861424", "Rick and Morty",     "$TMDB_IMAGE_BASE/8kOWDBK6XlPUzckuHDo3wwVRFwt.jpg",  "$TMDB_BACKDROP_BASE/venGA8dn0wS5nnMRlCHGIr79DjI.jpg", "2013–", "9.1", "Animation","7+ Seasons","An animated series that follows the misadventures of an alcoholic scientist and his grandson.", "$BASE_URL/watch/show/tt2861424", ContentType.TV_SHOW, "HD"),
    )

    private fun getByGenre(genre: String): List<Movie> {
        val all = getFeaturedMovies() + getTrendingMovies() + getNewReleases()
        return all.filter { it.genre.lowercase().contains(genre.lowercase()) }
            .ifEmpty { getTrendingMovies().take(6) }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    suspend fun search(query: String): List<Movie> = withContext(Dispatchers.IO) {
        val all = getFeaturedMovies() + getTrendingMovies() + getNewReleases() + getTvShows()
        all.filter { movie ->
            movie.title.lowercase().contains(query.lowercase()) ||
            movie.genre.lowercase().contains(query.lowercase()) ||
            movie.description.lowercase().contains(query.lowercase())
        }
    }

    // ── Movie Detail ──────────────────────────────────────────────────────────

    suspend fun getMovieDetail(id: String): Movie? = withContext(Dispatchers.IO) {
        val all = getFeaturedMovies() + getTrendingMovies() + getNewReleases() + getTvShows()
        all.find { it.id == id }
    }

    // ── Related Movies ────────────────────────────────────────────────────────

    suspend fun getRelatedMovies(movie: Movie): List<Movie> = withContext(Dispatchers.IO) {
        val all = getFeaturedMovies() + getTrendingMovies()
        all.filter { it.id != movie.id && it.genre == movie.genre }.take(8)
            .ifEmpty { getTrendingMovies().filter { it.id != movie.id }.take(8) }
    }
}
