import { NgModule } from '@angular/core';

import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: 'Student', loadChildren: () => import('./student/student.module').then(m => m.StudentModule) },
  { path: 'Student-Profile', loadChildren: () => import('./student-profile/student-profile.module').then(m => m.StudentProfileModule) },
  { path: 'Student-Details', loadChildren: () => import('./student-details/student-details.module').then(m => m.StudentDetailsModule) },
  { path: 'Hostel', loadChildren: () => import('./hostel/hostel.module').then(m => m.HostelModule) },
  { path: 'Student-list', loadChildren: () => import('./student-list/student-list.module').then(m => m.StudentListModule) },
  { path: 'Search', loadChildren: () => import('./search/search.module').then(m => m.SearchModule) },
  { path: 'Teacher-info', loadChildren: () => import('./teacher-info/teacher-info.module').then(m => m.TeacherInfoModule) },
  { path: 'Teacher-Details', loadChildren: () => import('./teacher-details/teacher-details.module').then(m => m.TeacherDetailsModule) },
  { path: 'Teacher-list', loadChildren: () => import('./teacher-list/teacher-list.module').then(m => m.TeacherListModule) },
  { path: 'Class-info', loadChildren: () => import('./class-info/class-info.module').then(m => m.ClassInfoModule) },
  { path: 'Subjects', loadChildren: () => import('./subjects/subjects.module').then(m => m.SubjectsModule) },
  { path: 'Attendance', loadChildren: () => import('./attendance/attendance.module').then(m => m.AttendanceModule) },
  { path: 'Payment', loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule) },
  { path: 'Sign-up', loadChildren: () => import('./sign-up/sign-up.module').then(m => m.SignUpModule) },
  { path: 'Log-in', loadChildren: () => import('./log-in/log-in.module').then(m => m.LogInModule) },
  { path: 'Forgot-password', loadChildren: () => import('./forgot-password/forgot-password.module').then(m => m.ForgotPasswordModule) },
  { path: 'Page-not-found', loadChildren: () => import('./page-not-found/page-not-found.module').then(m => m.PageNotFoundModule) },
  { path: '', redirectTo: 'Student', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
