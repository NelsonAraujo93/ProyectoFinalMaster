import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserSessionService } from '../services/user-session.service';
import { LoginForm } from '../app.models';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { BgCircleComponent } from '../layout/bg-circle/bg-circle.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgOptimizedImage, RouterLink, BgCircleComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private userSessionService: UserSessionService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const loginData: LoginForm = this.loginForm.value;
      this.userSessionService.login(loginData.username, loginData.password).subscribe({
        next: user => {
          if (user.userType === 'client') {
            this.router.navigate(['/client-dashboard']);
          } else if (user.userType === 'pyme') {
            this.router.navigate(['/pyme-dashboard']);
          }
        },
        error: err => {
          this.error = 'Invalid username or password', err;
        }
      });
    }
  }
}
