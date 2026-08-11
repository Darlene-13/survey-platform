import { Component, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-survey-response',
  imports: [FormsModule, RouterLink],
  templateUrl: './survey-response.component.html',
  styleUrl: './survey-response.component.scss'
})
export class SurveyResponseComponent {
  protected readonly step = signal(1);
  protected readonly submitted = signal(false);
  protected name = '';
  protected email = '';
  protected experience = '';
  protected tools = new Set<string>();
  protected feedback = '';
  protected readonly progress = computed(() => this.step() * 20);

  protected toggleTool(tool: string): void {
    const next = new Set(this.tools);
    next.has(tool) ? next.delete(tool) : next.add(tool);
    this.tools = next;
  }

  protected next(): void {
    if (this.step() < 5) this.step.update(value => value + 1);
    else this.submitted.set(true);
  }

  protected previous(): void {
    if (this.step() > 1) this.step.update(value => value - 1);
  }
}
