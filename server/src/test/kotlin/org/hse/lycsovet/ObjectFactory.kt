package org.hse.lycsovet

fun createUser(
    id: Long? = 1,
    login: String = "",
    role: Role = createRole(),
    grade: Int = 10,
    stream: Stream = Stream.UNKNOWN,
    name: String = ""
) = User(id, login, role, grade, stream, name)

fun createRole(
    id: Long? = 1,
    name: String = "",
    level: Int = 1
) = Role(id, name, level)

fun createAppeal(
    id: Long? = 1,
    title: String = "",
    text: String = "",
    user: User = createUser(),
    feedback: Int = 0,
    published: Boolean = true,
    type: AppealType = createAppealType(),
    status: AppealStatus = createAppealStatus()
) = Appeal(id, title, text, user, feedback, published, type, status)

fun createAppealType(
    id: Long? = 1,
    name: String = ""
) = AppealType(id, name)

fun createAppealStatus(
    id: Long? = 1,
    name: String = "",
    milestone: Long = 1
) = AppealStatus(id, name, milestone)