import { Routes } from '@angular/router';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { CommentsPageComponent } from './comments-page/comments-page.component';
import { LoginComponent } from './login/login.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
    {path: '',              component: WelcomePageComponent},
    {path: 'comments/:cat', component: CommentsPageComponent},
    {path: 'login',         component: LoginComponent},
    {path: 'manage-users',  component: ManageUsersComponent, canActivate: [authGuard]} 
];
