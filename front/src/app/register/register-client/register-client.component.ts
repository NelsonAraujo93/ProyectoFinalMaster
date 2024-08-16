import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSessionService } from '../../services/user-session.service';
import { Client } from '../../app.models';

@Component({
  selector: 'app-register-client',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register-client.component.html',
  styleUrls: ['./register-client.component.less']
})
export class RegisterClientComponent {
  @Input() goBack!: () => void;
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
      postalCode: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const clientData: Client = this.registerForm.value;
      this.userSessionService.registerClient(clientData).subscribe({
        next: (response) => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.error = 'Registration failed: ' + err.message;
        }
      });
    }
  }
}
