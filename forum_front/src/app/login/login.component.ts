import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../comments.service';
import { SignupData } from '../models/signup-data.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { EmailService } from '../email.service';
import { switchMap, map } from 'rxjs/operators';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, MatSnackBarModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit, OnDestroy{
  tmpUser: User | null = null;
  currentUser: User | null = null;
  private subscription: Subscription | null = null;

  twoStepCode: number = 0;


  constructor(private authService: AuthService,   private router: Router, 
              private http: HttpClient,           private cdr: ChangeDetectorRef,
              private emailService: EmailService, private snackBar: MatSnackBar,
              private route: ActivatedRoute){}

  
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        this.http.get(`https://localhost:8443/api/v1/auth/github-token-endpoint?code=${code}`, { observe: 'response', responseType: 'text' as 'json' }).subscribe(response => {
          
          console.log("Successfully logged in - First step");
          let username: string = response.body != null ? response.body.toString() : "";
          
          
          if (response.status === 200) {       // SECOND STEP STARTS
    
            // TEMPORARILY STORE CURRENT USER
            let params = new HttpParams().set('username', username);
            this.http.get<any>('https://localhost:8443/api/v1/users/searchByUsername', { params }).subscribe(user => {
              
              this.secondStepVerification(user.email).subscribe(proceed => {
                if (proceed) { // ------ LOGIN SUCCESS !!!
    
                  this.getJavaToken(user.id);
    
                  this.authService.currentUser = user;
                  console.log(this.authService.currentUser);
                  this.router.navigate(['']);
    
                } else {
                  console.log('Verification failed!');
                }
              });
    
            }
          );
          
          }
          
        }, (error) => {
          this.authService.currentUser = null;
          console.error('Login failed!', error);
          this.snackBar.open("Invalid Username/Password or Your Acount is Not Activated!", 'Close', { duration: 3000, });
        });

      }
    });


    this.subscription = this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.cdr.detectChanges(); // Manually trigger change detection
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
  



  onLogin(form: NgForm){
    // TODO: VALIDACIJA PODATAKA


    const username = form.value.logUsername;
    const password = form.value.logPassword;

    this.authService.login(username, password).subscribe(response => {
      console.log(response + " - First step");

      if (response === "Successfully logged in.") {       // SECOND STEP STARTS

        // TEMPORARILY STORE CURRENT USER
        let params = new HttpParams().set('username', username);
        this.http.get<any>('https://localhost:8443/api/v1/users/searchByUsername', { params }).subscribe(user => {
          
          this.secondStepVerification(user.email).subscribe(proceed => {
            if (proceed) { // ------ LOGIN SUCCESS !!!

              this.getJavaToken(user.id);

              this.authService.currentUser = user;
              console.log(this.authService.currentUser);
              this.router.navigate(['']);

            } else {
              console.log('Verification failed!');
            }
          });

        }
      );
    }
    }, (error) => {
      this.authService.currentUser = null;
      console.error('Login failed!', error);
      this.snackBar.open("Invalid Username/Password or Your Acount is Not Activated!", 'Close', {
        duration: 3000,
      });
    }
  
  );
  }


  secondStepVerification(to: string): Observable<boolean>{
    let subject: string = 'Two Step Verification for Online Forum'; 
    this.twoStepCode = Math.floor(10000 + Math.random() * 90000);
    let body: string = 'This is the verification code you need to enter to complete your login: ' + this.twoStepCode;

    return this.emailService.sendEmail(to, subject, body).pipe(
      switchMap(() => {
        console.log('Email sent successfully');
        return this.waitForUserInput(); // Wait for user input and return the result
      }),
      map(input => input === this.twoStepCode)
    );
  }
  

  waitForUserInput(): Observable<number> {
    return new Observable(observer => {
      const input = window.prompt('Check your email for the verification code:');
      if (input !== null && !isNaN(Number(input))) {
        observer.next(Number(input));
      } else {
        observer.next(0); // Default or error value if input is invalid
      }
      observer.complete();
    });
  }


  getJavaToken(userId: number){
    this.authService.getJWT(userId).subscribe(
      (response) => {
        localStorage.setItem('token', response.token);
        console.log("JWT saved.");
        // this.authService.clearTempUsername(); // Clear the temporary username
      }
    );
  }



  onSignup(form: NgForm){
    // TODO: VALIDATE DATA
    if(form.invalid){ return; }
    const signupData = new SignupData(form.value.signUsername, form.value.signPassword, form.value.email);

    this.authService.signup(signupData)
      .subscribe( (response) => {
        console.log(response);
        } ); 
    form.reset();
  }

  
  githubLogin(){
    window.location.href="https://github.com/login/oauth/authorize?client_id=Ov23liYFRQ9jiXfEM3a5&redirect_uri=https://localhost:4200/login&scope=user:email&prompt=login";
  }


}
