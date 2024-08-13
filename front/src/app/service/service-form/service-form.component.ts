import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Service } from '../../app.models';

@Component({
  selector: 'app-service-form',
  templateUrl: './service-form.component.html',
  styleUrls: ['./service-form.component.less'],
  standalone: true,
  imports: [ReactiveFormsModule]
})
export class ServiceFormComponent implements OnInit {
  @Input() service: Service | null = null;
  @Output() save = new EventEmitter<Service>();
  @Output() cancel = new EventEmitter<void>();

  serviceForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.serviceForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    if (this.service) {
      this.serviceForm.patchValue(this.service);
    }
  }

  onSubmit(): void {
    if (this.serviceForm.valid) {
      this.save.emit(this.serviceForm.value);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
