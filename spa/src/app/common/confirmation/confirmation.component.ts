import { Component, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'app-confirmation',
    templateUrl: './confirmation.component.html',
    styleUrls: ['./confirmation.component.less']
})
export class ConfirmationComponent {

    @Output()
    isPromptVisibleAndAnswer = new EventEmitter<any>();

    constructor() {
    }

    setAnswer(answer: boolean): void {
        this.isPromptVisibleAndAnswer.emit({
                isPromptVisible: false,
                response: answer
            }
        );
    }
}
