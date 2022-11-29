declare namespace API{
    export interface dynamicType {
        "dynamic": {
            "id": number,
            "title": string,
            "content": string,
            "likeNum": number,
            "commentNum": number,
            "shareNum": number,
            "userId": number,
            "topicId": number,
            "classifyId": number,
            "publishTime": string
        },
        "images": [
            {
                "id": number,
                "reportId": number,
                "dynamicId": number,
                "url": string,
                "sort": number
            }
        ],
        "topic": {
            "id": number,
            "title": string,
            "describe": string,
            "concernNum": number
        }
    }
}