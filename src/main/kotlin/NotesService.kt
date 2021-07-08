import java.util.Comparator

data class Note(
    val id: Int = 0,
    val ownerId: Int,
    val title: String, //заголовок заметки.
    val text: String, //текст заметки.
    val privacy: Privacy, //уровень доступа к заметке.
    val comment_privacy: Privacy, //уровень доступа к комментированию заметки.
)

data class Comment(
    val commentId: Int = 0,
    val noteId: Int, //идентификатор заметки.
    val ownerId: Int, //идентификатор владельца заметки.
    val replyTo: Int?, //идентификатор пользователя, ответом на комментарий которого является добавляемый комментарий // (не передаётся, если комментарий не является ответом).
    val message: String, //текст комментария.
    val delete: Boolean = false //меняется на true, если комментарий был удален
)

enum class Privacy {
    ALL, // все пользователи
    FRIENDS, // только друзья
    FRIENDS_OF_FRIENDS, // друзья и их друзья
    USER //  только пользователь
}

enum class Sort { DECREASE, INCREASE }

class NotesService {

    var notes = mutableListOf<Note>()
    var comments = mutableListOf<Comment>()

    //Создает новую заметку у текущего пользователя.
    fun add(note: Note) {
        notes.plusAssign(note.copy(id = if (notes.isNotEmpty()) notes.last().id + 1 else 1))
//        return notes.last()
    }

    //Добавляет новый комментарий к заметке.
    fun createComment(comment: Comment) {
        for (note in notes)
            if (comment.noteId == note.id) {
                comments.plusAssign(comment.copy(commentId = if (comments.isNotEmpty()) comments.last().commentId + 1 else 1))
                return
            }
        println("Заметки не существует")
    }

    //Удаляет заметку текущего пользователя.
    fun delete(noteId: Int) {
        for (note in notes)
            if (note.id == noteId) {
                notes.remove(note)
                return
            }
        println("Заметки не существует")
    }

    //Удаляет комментарий к заметке.
    fun deleteComment(commentId: Int, ownerId: Int) {
        for ((index, comment) in comments.withIndex())
            if (comment.commentId == commentId && comment.ownerId == ownerId) {
                comments[index] = comment.copy(delete = true)
                return
            }
        println("Комментарий не существует")
    }


    //Редактирует заметку текущего пользователя.
    fun edit(note: Note) {
        for ((index, element) in notes.withIndex())
            if (element == note)
                notes[index] = note
    }

    //Редактирует указанный комментарий у заметки.
    fun editComment(commentId: Int, ownerId: Int, message: String) {
        for ((index, comment) in comments.withIndex())
            if (comment.commentId == commentId && comment.ownerId == ownerId)
                comments[index] = comment.copy(message = message)
    }

    //Возвращает список заметок, созданных пользователем.
    fun get(
        noteId: List<Int>, //идентификаторы заметок, информацию о которых необходимо получить.
        userId: Int, //идентификатор пользователя, информацию о заметках которого требуется получить.
        count: Int = 20, //количество заметок, информацию о которых необходимо получить. По умолчанию 20, максимальное значение 100
        sort: Sort = Sort.INCREASE // сортировка результатов)
    ): List<Note>? {
        val listNotesCount = mutableListOf<Note>()
        val fromInt = if (count > notes.size) notes.size else count
        return try {
            for ((index, note) in notes.withIndex())
                if (note.ownerId == userId)
                    for (id in noteId)
                        if (note.id == id)
                            listNotesCount.plusAssign(notes[index])
        return if (sort == Sort.DECREASE) listNotesCount.sortedByDescending { note -> note.id }.subList(0, fromInt)
        else listNotesCount.subList(0, fromInt)
        } catch (e: IndexOutOfBoundsException){
            println("Заметки отсутствуют")
            null
        }
    }

    //Возвращает заметку по её id.
    fun getById(noteId: Int, userId: Int): Note? {
        for ((index, note) in notes.withIndex())
            if (note.ownerId == userId && note.id == noteId)
                return notes[index]
        return null
    }

    //Возвращает список комментариев к заметке.
    fun getComments(
        noteId: Int,
        ownerId: Int,
        sort: Sort = Sort.INCREASE,
        count: Int = 20
    ): List<Comment>? {
        val listCommentsCount = mutableListOf<Comment>()
        val fromInt = if (count > comments.size) comments.size else count

        return try {for ((index, comment) in comments.withIndex())
            if (comment.ownerId == ownerId && comment.noteId == noteId) {
                listCommentsCount.plusAssign(comments[index])
            }
            if (sort == Sort.DECREASE) listCommentsCount.sortedByDescending { comment -> comment.commentId }.subList(0, fromInt)
            else listCommentsCount.subList(0, fromInt)

        } catch (e: IndexOutOfBoundsException){
            println("Комментария не существует")
            null
        }
    }

    //Восстанавливает удалённый комментарий.fun
    fun restoreComment(commentId: Int, ownerId: Int) {
        for ((index, comment) in comments.withIndex())
            if (comment.commentId == commentId && comment.ownerId == ownerId)
                comments[index] = comment.copy(delete = false)
    }

    fun printNotes() {
        for (note in notes)
            println(note)
    }

    fun printComments() {
        for (comment in comments)
            if (!comment.delete)
                println(comment)
    }
}

