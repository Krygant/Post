package ru.netology

class Attachment {
    interface Attachments {
        val type: String
    }

    data class Photo(
        val id: Int,            //Идентификатор записи
        val ownerId: Int,       //Идентификатор владельца записи
        val photo130: String,   //Ссылка на фото
        val photo604: String    //Ссылка на фото
    )

    data class PhotoAttachment(val photo: Photo): Attachments{
        override val type = "type"
    }

    data class Video(
        val id: Int,        //Идентификатор записи
        val ownerId: Int,   //Идентификатор владельца записи
        val title: String,  //Название видео
        val duration: Int   //Продолжительность видео
    )

    data class VideoAttachment(val video: Video): Attachments{
        override val type = "type"
    }

    data class Audio(
        val id: Int,        //Идентификатор записи
        val ownerId: Int,   //Идентификатор владельца записи
        val artist: String, //Исполнитель
        val title: String   //Название композиции
    )

    data class AudioAttachment(val audio: Audio): Attachments{
        override val type = "type"
    }

    data class Document(
        val id: Int,        //Идентификатор документа
        val ownerId: Int,   //Идентификатор пользователя, загрузившего документ
        val title: String,  //Название документа
        val size: Int       //Размер документа в байтах
    )

    data class DocumentAttachment(val document: Document): Attachments{
        override val type = "type"
    }

    data class Link(
        val url: String,            //URL ссылки
        val title: String,          //Заголовок ссылки
        val caption: String,        //Подпись ссылки(если имеется)
        val description: String     //Описание ссылки
    )

    data class LinkAttachment(val link: Link): Attachments{
        override val type = "type"
    }

}