
object Backup {
    suspend fun export(c: Context): String {
        val db = AppDatabase.get(c)
        val tasks = JSONArray()
        for (t in db.studyTaskDao().observeAll().first()) {
            tasks.put(JSONObject().put("title", t.title).put("subject", t.subject)
                .put("due", t.dueEpochDay ?: JSONObject.NULL).put("done", t.isDone))
        }

object Backup {
    suspend fun export(c: Context): String {
        val db = AppDatabase.get(c)
        val tasks = JSONArray()
        for (t in db.studyTaskDao().observeAll().first()) {
            tasks.put(JSONObject().put("title", t.title).put("subject", t.subject)
                .put("due", t.dueEpochDay ?: JSONObject.NULL).put("done", t.isDone))
        }
        val exams = JSONArray()
        for (e in db.examDao().observeAll().first()) {
            exams.put(JSONObject().put("name", e.name).put("date", e.dateEpochDay))
        }
        return JSONObject().put("app", "FutureX").put("tasks", tasks).put("exams", exams).toString()
    }

    suspend fun restore(c: Context, text: String): Int {
        val db = AppDatabase.get(c)
        val root = JSONObject(text)
        var added = 0
        val oldTasks = db.studyTaskDao().observeAll().first()
        val oldExams = db.examDao().observeAll().first()
        val ta = root.optJSONArray("tasks") ?: JSONArray()
        for (i in 0 until ta.length()) {
            val o = ta.getJSONObject(i)
            val due = if (o.isNull("due")) null else o.getLong("due")
            val t = StudyTask(title = o.getString("title"), subject = o.getString("subject"),
                dueEpochDay = due, isDone = o.getBoolean("done"))
            if (oldTasks.none { it.title == t.title && it.subject == t.subject && it.dueEpochDay == t.dueEpochDay }) {
                db.studyTaskDao().insert(t); added++
            }
        }
        val ea = root.optJSONArray("exams") ?: JSONArray()
        for (i in 0 until ea.length()) {
            val o = ea.getJSONObject(i)
            val e = Exam(name = o.getString("name"), dateEpochDay = o.getLong("date"))
            if (oldExams.none { it.name == e.name && it.dateEpochDay == e.dateEpochDay }) {
                db.examDao().insert(e); added++
            }
        }
        return added
    }
}
