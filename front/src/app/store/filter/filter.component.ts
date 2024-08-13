// filter.component.ts
import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.less']
})
export class FilterComponent {
  @Output() filterApplied = new EventEmitter<any>();
  @Output() clearFilter = new EventEmitter<void>();

  filterForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      name: [''],
      minPrice: [''],
      maxPrice: [''],
      minRating: ['']
    });
  }

  onFilter(): void {
    this.filterApplied.emit(this.filterForm.value);
  }

  onClear(): void {
    this.filterForm.reset();
    this.clearFilter.emit(); // Emit an event to clear the filter
  }
}