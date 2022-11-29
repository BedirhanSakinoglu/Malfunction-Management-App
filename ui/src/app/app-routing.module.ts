import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailsComponent } from './details/details.component';
import { ErrorComponent } from './error/error.component';
import { AuthGuard } from './guard/auth.guard';
import { HomeComponent } from './home/home.component';
import { MalfunctionsComponent } from './malfunctions/malfunctions.component';
import { UpdateComponent } from './update/update.component';

const routes: Routes = [
  { path: '', component: MalfunctionsComponent , canActivate: [AuthGuard]},
  //{path:'home', component:HomeComponent , canActivate: [AuthGuard]},
  {path:'malfunctions', component:MalfunctionsComponent , canActivate: [AuthGuard]},
  {path:'**', component:ErrorComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
