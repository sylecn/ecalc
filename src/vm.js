/*
 * the virtual machine can work on its own without ecalc UI. But it remains in
 * ecalc.vm namespace.
 */
if (typeof ecalc === 'undefined') {
    var ecalc = {};
    alert('Warning: ecalc not defined.');
}

ecalc.vm = {
    log: function (msg) {
	ecalc.log(msg);
    },
    /**
     * constructor for a History
     */
    History: function () {
	// pretty print Operators
	var printOp = function (op) {
	    switch (op) {
	    case 'ADD': return '+';
	    case 'SUBTRACT': return '-';
	    default: return '';
	    }
	};
	// pretty print result, keep 2 digits after point
	var printResult = function (number) {
	    var format = /[^.]*(\.\d{0,2})?/;
	    return format.exec(number + '')[0];
	};
	this._all = [{
	    // _numberStack
	    nS: [],
	    // _resultStack
	    rS: [],
	    // _opStack
	    oS: []
	}];
	// this.current = ;
	this.pushNumber = function (number) {
	    this._all.last().nS.push(number);
	};
	this.pushResult = function (number) {
	    this._all.last().rS.push(number);
	};
	this.pushOp = function (op) {
	    this._all.last().oS.push(op);
	};
	this.calcDone = function () {
	    // keep only latest 10
	    if (this._all.length > 10) {
		utils.assert(false, 'history._all should never have more than\
10 members');
	    }
	    if (this._all.length == 10) {
		this._all.shift();
	    }
	    this._all.push({
		// _numberStack
		nS: [],
		// _resultStack
		rS: [],
		// _opStack
		oS: []
	    });
	};
	/**
	 * get a rS stack from nS and oS.
	 * @return rS stack
	 */
	this.compute = function (nS, oS) {
	    var rS = [];
	    var result;
	    if (nS.length > 1) {
		utils.assert(oS.length >= 1,
			     'compute: oS should have at least 1 op');
		// first result
		switch (oS[0]) {
		case 'ADD':
		    result = nS[0] + nS[1];
		    break;
		case 'SUBTRACT':
		    result = nS[0] - nS[1];
		    break;
		default:
		    // never reacher
		    utils.assert(false,
				 'compute(): function for Key [' +
				 key + '] Not implemented.');
		}
		rS[0] = result;
	    }
	    for (var i = 1; i < oS.length; ++i) {
		switch (oS[i]) {
		case 'ADD':
		    result = rS[i - 1] + nS[i + 1];
		    break;
		case 'SUBTRACT':
		    result = rS[i - 1] - nS[i + 1];
		    break;
		default:
		    // never reacher
		    utils.assert(false,
				 'compute(): function for Key [' +
				 oS[i] + '] Not implemented.');
		}
		rS.push(result);
	    }
	    return rS;
	};
	/**
	 * recompute this._all[index] using nS and oS.
	 */
	this.recompute = function (index) {
	    var nS = this._all[index].nS;
	    var oS = this._all[index].oS;
	    var rS = this.compute(nS, oS);
	    this._all[index].rS = rS;
	};
	/**
	 * print one history session in this._all, optionally compare it with
	 * compareTo session.
	 */
	this._oneCalcAsHTML = function(index, compareTo) {
	    var nS = this._all[index].nS;
	    var oS = this._all[index].oS;
	    var rS = this._all[index].rS;
	    var table = '<table class="history">';
	    utils.assert(utils.isDefined(compareTo),
			 '_oneCalcAsHTML(): comparedTo should be defined');
	    if (compareTo !== false) {
		// use colors to mark diff.
		for (var i = 0; i < nS.length; i++) {
		    table += [
			'<tr><td class="left ',
			nS[i] === compareTo.nS[i] ? 'match': 'diff',
			'" data-x="', index,
			'" data-y="', i,
			'" data-type="nS"',
			'">',
			nS[i],
			'</td>',
			'<td class="right result ',
			rS[i - 1] === compareTo.rS[i - 1] ? 'match': 'diff',
			'">',
			printResult(rS[i - 1] || ''),
			'</td></tr><tr><td class="left ',
			oS[i] === compareTo.oS[i] ? 'match': 'diff',
			'" data-x="', index,
			'" data-y="', i,
			'" data-type="oS"',
			'">',
			printOp(oS[i] || ''),
			'</td><td class="right"></td></tr>'].join('');
		};
	    } else {
		for (var i = 0; i < nS.length; i++) {
		    table += ['<tr><td class="left">', nS[i], '</td>',
			      '<td class="right result">',
			      printResult(rS[i - 1] || ''),
			      '</td></tr><tr><td class="left">',
			      printOp(oS[i] || ''),
			      '</td><td class="right"></td></tr>'].join('');
		};
	    }
	    table += '</table>';
	    return table;
	};
	// show most recent histories.
	this.asHTML = function () {
	    var nS, oS, rS;
	    // show howMany sessions? default is 3.
	    var howMany = 3;
	    var table = [];
	    var len = this._all.length;
	    if (! this._all.last().nS.length) {
		len -= 1;
	    }
	    if (len >= 2) {
		// compare the last session to last but 1 session.
		table.push('<td class="top">' +
			   this._oneCalcAsHTML(len - 1,
					       this._all[len - 2]) +
			   '</td>');
		howMany -= 1;
		len -= 1;
	    }
	    if (len < howMany) {
		howMany = len;
	    }
	    for (var i = 1; i <= howMany; ++i) {
		table.push('<td class="top">' +
			   this._oneCalcAsHTML(len - i, false) +
			   '</td>');
	    }
	    return  '<div>History<table id="all-history"><tr>' +
		table.join('') +
		// table.reverse().join('') +
		'</tr></table></div>';
	};
	// clear everything
	this.clear = function () {
	    this._all = [{
		// _numberStack
		nS: [],
		// _resultStack
		rS: [],
		// _opStack
		oS: []
	    }];
	};
    },
    /**
     * constructor for a VirtualMachine.
     */
    VirtualMachine: function (name) {
	var _virtualKeys = [
	    // ================
	    //  basic features
	    // ================
	    // digits
	    'NUM0', 'NUM1', 'NUM2', 'NUM3', 'NUM4',
	    'NUM5', 'NUM6', 'NUM7', 'NUM8', 'NUM9',
	    'DOT', 'NUM00',
	    // op
	    'ADD', 'SUBTRACT',
	    // misc
	    'BACKSPACE', 'EQUAL', 'CLEAR', 'RESET'
	    // ===================
	    //  advanced features
	    // ===================
	];

	/**
	 * reset partial number states
	 */
	this._resetPartialNumber = function () {
	    this._acceptDot = true;
	    this._partialNumber = '';
	}

	/**
	 * accept partial number.
	 */
	this._acceptPartialNumber = function () {
	    var number = parseFloat(this._partialNumber) || 0;
	    this._numberStack.push(number);
	    this._resetPartialNumber();

	    this.history.pushNumber(number);
	};

	/**
	 * CLEAR key pressed
	 */
	this._clear = function () {
	    // key stack
	    this._keyStack = [];
	    // stacks
	    this._numberStack = [];
	    this._opStack = [];

	    this._resetPartialNumber();
	    this._calcDone = false;
	    this._partialCalcDone = false;
	};

	/**
	 * if opStack is not empty. do calculation.
	 */
	this._doCalculationMaybe = function () {
	    var value1, value2, result;

	    op = this._opStack.pop();
	    if (op) {
		// calculate for last OP in stack
		value2 = this._numberStack.pop();
		value1 = this._numberStack.pop();
		switch (op) {
		case 'ADD':
		    result = value1 + value2;
		    break;
		case 'SUBTRACT':
		    result = value1 - value2;
		    break;
		default:
		    utils.assert(false, '_doCalculationMaybe(): bad op: ' + op);
		}
		this._numberStack.push(result);
		this._partialCalcDone = true;
		this.history.pushResult(result);
	    }
	};

	/**
	 * screen obejct
	 */
	// Don't know how to know the outter 'this' in inner object.
	// the following doesn't work.
	// this.screen = function () {
	//     var that = this;

	//     return {
	// 	asText: function () {
	// 	    return that._partialNumber || '0';
	// 	}
	//     };
	// } ();

	this.screen_asText = function () {
	    if (this._calcDone || this._partialCalcDone) {
		return this._numberStack.last();
	    } else {
		return this._partialNumber || '0';
	    }
	};

	// trivial
	this.name = name;
	this._keyStack = [];

	// stacks
	this._numberStack = [];
	this._opStack = [];

	// state control
	this._calcDone = false;
	this._acceptDot = true;
	this._partialNumber = '';

	// history
	this.history = new ecalc.vm.History();

	// ==================
	//  public functions
	// ==================

	// represent this VM states in HTML
	this.asHTML = function () {
	    return '<div class="vm">' + name +
		// show last 10 Key
		'<br/>keyStack: ' + this._keyStack.slice(-10).join(' ') +
		'<br/>_numberStack: ' + this._numberStack.join(' ') +
		'<br/>_opStack: ' + this._opStack.join(' ') +
		'<br/>_partialNumber: ' + this._partialNumber +
		'<br/>_calcDone: ' + this._calcDone +
		'</div>';
	};
	/**
	 * press key on virtual machine.
	 */
	this.pressKey = function (key) {
	    var value1, value2, op;
	    utils.assertTrue(utils.inList(key, _virtualKeys),
			    'pressKey(): bad key: ' + key);
	    this._keyStack.push(key);

	    switch (key) {
	    case 'CLEAR':
		this._clear();
		break;
	    case 'RESET':
		this._clear();
		this.history.clear();
		break;
	    case 'NUM0':  //all all the way through
	    case 'NUM00':
	    case 'NUM1':
	    case 'NUM2':
	    case 'NUM3':
	    case 'NUM4':
	    case 'NUM5':
	    case 'NUM6':
	    case 'NUM7':
	    case 'NUM8':
	    case 'NUM9':
		if (key == 'NUM0' || key == 'NUM00') {
		    if (this._partialNumber) {
			this._partialNumber += key.slice(3);
		    } else {
			ecalc.vm.log('ignore none significant ' + key);
		    }
		} else {
		    this._partialNumber += key.slice(3);
		}

		this._partialCalcDone = false;
		if (this._calcDone) {
		    // throw away non-reachable data in stack
		    this._numberStack = [];
		    this._opStack = [];
		    this._calcDone = false;
		}
		break;
	    case 'DOT':
		if (this._acceptDot) {
		    if (this._partialNumber) {
			this._partialNumber += '.';
		    } else {
			this._partialNumber = '0.';
		    }
		    this._acceptDot = false;
		} else {
		    ecalc.vm.log('Warning: ignore nonsense DOT.');
		}
		break;
	    case 'BACKSPACE':
		if (this._calcDone || this._partialCalcDone) {
		    ecalc.vm.log('Warning: ignore nonsense BACKSPACE.');
		} else {
		    if (this._partialNumber.last() === '.') {
			this._acceptDot = true;
			if (this._partialNumber === '0.') {
			    this._partialNumber = '';
			}
		    }
		    this._partialNumber = this._partialNumber.slice(0, -1);
		}
		break;
	    case 'EQUAL':
		if (! this._calcDone) {
		    this._acceptPartialNumber();
		    this._doCalculationMaybe();
		    this._calcDone = true;
		    this.history.calcDone();
		} else {
		    ecalc.vm.log('Warning: ignore nonsense EQUAL.');
		}
		break;
	    case 'ADD':
	    case 'SUBTRACT':
		if (this._calcDone) {
		    this._calcDone = false;
		} else {
		    this._acceptPartialNumber();
		}
		this._doCalculationMaybe();

		this._opStack.push(key);
		this.history.pushOp(key);

		break;
	    default:
		// never reacher
		utils.assert(false,
			     'pressKey(): function for Key [' + key +
			     '] Not implemented.');
	    }
	};
    }
};
