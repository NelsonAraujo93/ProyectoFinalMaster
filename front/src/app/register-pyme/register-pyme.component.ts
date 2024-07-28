import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSessionService } from '../services/user-session.service';
import { Pyme } from '../app.models';

@Component({
  selector: 'app-register-pyme',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register-pyme.component.html',
  styleUrls: ['./register-pyme.component.less']
})
export class RegisterPymeComponent {
  registerForm: FormGroup;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private userSessionService: UserSessionService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      dni: ['', [Validators.required]],
      postalCode: ['', [Validators.required]],
      pymePostalCode: ['', [Validators.required]],
      pymePhone: ['', [Validators.required]],
      pymeName: ['', [Validators.required]],
      pymeDescription: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const pymeData: Pyme = {
        ...this.registerForm.value,
        services: []
      };
      this.userSessionService.registerPyme(pymeData).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: err => {
          this.error = 'Registration failed', err;
        }
      });
    }
  }
}
