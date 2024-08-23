import { ChangeDetectorRef, Component, NgModule } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { User } from '../comments.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { UserService } from '../user.service';
import { EmailService } from '../email.service';

@Component({
  selector: 'app-manage-users',
  standalone: true,
  imports: [MatCardModule, MatTableModule, 
            MatFormFieldModule, MatButtonModule, MatInputModule, 
            MatSelectModule, MatCheckboxModule, FormsModule,
            CommonModule],
  templateUrl: './manage-users.component.html',
  styleUrl: './manage-users.component.css'
})
export class ManageUsersComponent {
  users: User[] = [];
  displayedColumns: string[] = ['userName','email','roleColumn', 'sciencePermissions','culturePermissions',
                                'sportPermissions','musicPermissions', 'activate'];
  active: boolean = false;



  constructor(private userService: UserService, private changeDetectorRef: ChangeDetectorRef, 
                private location: Location, private emailService: EmailService){}

  ngOnInit(){
    this.loadUsersFromDB();
  }



   loadUsersFromDB() {
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
    });
  } 



  onRoleChange(user: User){
    this.userService.updateRole(user.id, user.role);
  }


  onPermissionChange(userId: number, categoryId: number, type: string){
    this.userService.updatePermission(userId, categoryId, type);
  }


  toggleActive(user: User){
    let body: string;

    this.userService.updateStatus(user.id);
    console.log('Sending email.');
    //this.refreshPage();

    if(user.status == 'BLOCKED'){
      body = 'Your Online Forum account has been ACTIVATED. You can log in now!';
      this.sendEmail(user.email, body);
    }
    else if( user.status === 'ACTIVE' ){
      body = 'Your Online Forum account has been BLOCKED. You can not log in now!';
      this.sendEmail(user.email, body);
    }
  }

  refreshPage() {
    this.location.go(this.location.path());
    window.location.reload();
  }

  sendEmail(recipient: string, body: string){
    const subject = 'Online Forum Account';
    this.emailService.sendEmail(recipient, subject, body).subscribe(() => {
      console.log('Email sent successfully');
    });
  }



  isBlocked(status: string): boolean {
    return status === 'BLOCKED';
  }

}


