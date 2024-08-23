import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { CommentsService, Comments, Page, User } from '../comments.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';
import { AuthService } from '../auth.service';
import { SubmitCommentData } from '../models/submit-comment-data.model';
import { Subscription } from 'rxjs';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';


@Component({
  selector: 'app-comments-page',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule, MatSnackBarModule],
  providers: [DatePipe],
  templateUrl: './comments-page.component.html',
  styleUrls: ['./comments-page.component.css']
})
export class CommentsPageComponent implements OnInit, OnDestroy{
  currentUser: User | null = null;
  private subscription: Subscription | null = null;

  category: string = '';
  data: any;

  comments: Comments[] = [];
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;

  categoryNumber: number = 0;   // Maps category name to its position in the permissions array


  constructor(
    private commentService: CommentsService, 
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    public authServ: AuthService,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar) { }

    
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.category = params.get('cat') || '';
      this.loadComments(this.category);
    });
    
    this.subscription = this.authServ.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.cdr.detectChanges(); // Manually trigger change detection
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
  


  loadComments(cat: string): void {
    let catId = 1;

    if (cat === 'science') {
      catId = 1;
    } else if (cat === 'culture') {
      catId = 2;
    } else if (cat === 'sports') {
      catId = 3; 
    }
    else if (cat == 'music'){
      catId = 4;
    }
    else console.log("Wrong category type");

    this.categoryNumber = catId - 1;

    this.commentService.getComments(catId, this.currentPage, this.pageSize).subscribe((data: Page<Comments>) => {
      this.comments = data.content;
      this.totalPages = data.totalPages;
      this.formatCommentsDate();

      this.comments.forEach(comm => {comm.editing = false});      // True when I click on the EDITING icon

    });

  }


  formatCommentsDate(): void {
    this.comments.forEach(comment => {
      comment.createdAt = this.datePipe.transform(comment.createdAt, 'dd. MMMM y, h:mm a') || 'Invalid Date';
    });
  }



  goToPage(page: number): void {
    this.currentPage = page;
    this.loadComments(this.category);
  }

  submitComment(form: NgForm){
    if(form.valid && this.currentUser != null){
      let submitCommentData = new SubmitCommentData(form.value.newComment, this.category, this.currentUser?.username);
      this.commentService.uploadComment(submitCommentData).subscribe(response => {
        console.log(response);
        this.loadComments(this.category);
      });
    }
    form.reset();
  }


  onEditComment(comment: Comments){
    if(this.currentUser != null && this.currentUser.permissionsList[this.categoryNumber].edit === true){
      comment.editing = true;
    }
    else{
      this.snackBar.open("You do not have permission to edit comments!", 'Close', {
        duration: 3000,
      });
    }
    
  }

  onSaveComment(comment: Comments){
    // TODO: VALIDATE DATA
    comment.editing = false;

    if(comment.text != null){  
      this.commentService.updateComment(comment.id, comment.text).subscribe(response => {
        console.log(response);
      });
    } else{
      console.log("No comment data!");
    }
    
  }



  onDeleteComment(id: number){
    if(this.currentUser != null && this.currentUser.permissionsList[this.categoryNumber].delete === true){
      this.commentService.deleteComment(id).subscribe(response => {
        console.log(response);
        this.loadComments(this.category);
      })
    }
    else{
      this.snackBar.open("You do not have permission to delete comments!", 'Close', {
        duration: 3000,
      });
    }

  }


  cancelChanges(comment: Comments){
    comment.editing = false;
  }

}