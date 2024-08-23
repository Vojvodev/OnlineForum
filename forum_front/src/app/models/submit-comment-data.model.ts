export class SubmitCommentData {
    commentText: string;
    category: string; 
    currentUsername: string | undefined;

    constructor(commentText: string, category: string, currentUsername: string | undefined){
        this.commentText = commentText;
        this.category = category;
        this.currentUsername = currentUsername
    }
}
