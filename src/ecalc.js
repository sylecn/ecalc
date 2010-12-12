var ecalc = {
    // debug control variables
    debugKeyEvent: false,
    /**
     * general log function, used for debugging.
     */
    log: function (msg) {
	var old = $('#log').text();
	$('#log').text(old + msg + '\n');
    },
    clearLog: function () {
	$('#log').text('');
    },
    /**
     translate keyCode to virtual key in ecalc.vm
     acceptable keyCode:
     | keyboard key |         keyCode | virtualKey in vm |
     |--------------+-----------------+------------------|
     | numbers      |           48-57 | NUM0 to NUM9     |
     | .            |             190 | DOT              |
     | +            | 61 (with shift) | ADD              |
     | -            |             109 | SUBTRACT         |
     | BACKSPACE    |               8 | BACKSPACE        |
     | ENTER        |              13 | EQUAL            |
     | =            |              61 | EQUAL            |
     | c            |              67 | CLEAR            |
     */
    translate: function (event) {
	var keyCode = event.keyCode;
	var shiftKey = event.shiftKey;
	if ((keyCode >= 48) && (keyCode <= 57)) {
	    return 'NUM' + (keyCode - 48);
	}
	switch (keyCode) {
	case 190: return 'DOT';
	case 61:
	    if (shiftKey) {
		return 'ADD';
	    } else {
		return 'EQUAL';
	    }
	case 109: return 'SUBTRACT';
	case 8: return 'BACKSPACE';
	case 13: return 'EQUAL';
	case 67: return 'CLEAR';
	default: return undefined;
	}
    },
    keyup: function (event) {
	var key = event.keyCode;
	if (this.debugKeyEvent) {
	    this.log('keyup event: keyCode = ' + key);
	}
    },
    /**
     * the main dispatcher. listen to keyboard events.
     */
    keydown: function (event) {
	if (this.debugKeyEvent) {
	    this.log('keydown event: keyCode = ' + event.keyCode);
	}
	if (event.keyCode === 27) {
	    // ESC clear web page, not passed to VM.
	    this.clearLog();
	    return;
	}

	var key = this.translate(event);
	if (key) {
	    this.defaultVM.pressKey(key);
	} else {
	    switch (event.keyCode) {
	    case 16:
	    case 17:
	    case 18:
	    case 20:
		// modifiers
		break;
	    default:
		this.log('key ignored, keyCode = ' + event.keyCode);
	    }
	}
	this.updateUI();
    },
    keypress: function (event) {
	var key = event.charCode;
	if (this.debugKeyEvent) {
	    this.log('keypress event: charCode = ' + key);
	}
    },
    updateUI: function () {
	$('#screen').text(this.defaultVM.screen_asText());
	$('#vms').html(this.defaultVM.asHTML());
	$('#history').html(this.defaultVM.history.asHTML());
    },
    // functions used in HTML
    /**
     * toggle debug key event.
     */
    toggleDebugKeyEvent: function () {
	this.debugKeyEvent = $('#debug_key_event')[0].checked;
    },

    // test only
    makeTestInputOneToFive: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputBusinessData: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM8');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('EQUAL');
    }
};

// main
$(document).ready(function () {
    ecalc.log("ecalc ready.");

    /**
     * virtual machine
     */
    ecalc.defaultVM = new ecalc.vm.VirtualMachine("defaultVM");
    ecalc.updateUI();

    ecalc.makeTestInputOneToFive();
    ecalc.makeTestInputBusinessData();
    ecalc.updateUI();
});
